package com.kbds.gateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter;
import com.kbds.gateway.utils.StringUtils;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  Class Name     : RoutingBean.java
 *  Description    : SPC에서 실제 라우팅 서비스들을 관리하는 Bean을 가진 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
@ConfigurationProperties(prefix = "gateway")
@Data
@Slf4j
public class RoutingConfiguration {

  private final Logger logger = LoggerFactory.getLogger(RoutingConfiguration.class);

  private ApplicationContext appContext;
  private CachingRequestBodyFilter cachingRequestBodyFilter;
  private ObjectMapper objectMapper;

  /* Routes,Filter 관리 서버 주소 */
  @Value("${services.api.route-url}")
  String routeSeverUrl;

  /* Default Routing Info */
  private Map<String, Object> service;

  private final String PATH = "path";
  private final String URL = "url";
  private final String FILTER = "filter";

  /**
   * Constructor Injection
   *
   * @param appContext               스프링 context
   * @param cachingRequestBodyFilter 캐싱 Body 데이터
   * @param objectMapper             ObjectMapper 객체
   */
  @Autowired
  RoutingConfiguration(ApplicationContext appContext,
      CachingRequestBodyFilter cachingRequestBodyFilter, ObjectMapper objectMapper) {
    this.appContext = appContext;
    this.cachingRequestBodyFilter = cachingRequestBodyFilter;
    this.objectMapper = objectMapper;
  }

  /**
   * Routing 정보를 조회 한 후 필터와 함께 GATEWAY에 등록 하는 메소드 메인 메소드
   *
   * @param builder Router 객체
   * @return Router 정보
   */
  @Bean
  @RefreshScope
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {

    RouteLocatorBuilder.Builder routeLocator = builder.routes();
    List<RoutingDTO> routingDTOList;

    try {

      routeLocator = registerDefaultService(routeLocator);

      /* Routing 관리 서버로부터 API 목록을 가져 온다. */
      Mono<ResponseDTO> responseDTO =
          WebClient.create()
              .get()
              .uri(routeSeverUrl)
              .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNA== ")
              .retrieve().bodyToMono(ResponseDTO.class);

      routingDTOList = new ObjectMapper().convertValue(
          Objects.requireNonNull(responseDTO.block()).getResultData(),
          new TypeReference<List<RoutingDTO>>() {
          });
    } catch (Exception e) {

      logger.error(e.toString());
      return routeLocator.build();
    }

    // 실제 Routing 서비스, 필터 들을 등록 한다.
    for (RoutingDTO routingDTO : routingDTOList) {

      try {

        registerRouterLocator(routeLocator, routingDTO);
      } catch (Exception e) {

        logger.error(e.toString());
      }
    }

    return routeLocator.build();
  }

  /**
   * Gateway 시스템 기본 필수 Routing 정보 등록
   *
   * @param routeLocator Routing 관리 객체
   * @return Builder
   */
  public Builder registerDefaultService(Builder routeLocator) {

    service.forEach((key, value) -> {

      try {

        Map<String, String> defaultPath = objectMapper
            .convertValue(value, new TypeReference<Map<String, String>>() {
            });

        final String filter = defaultPath.getOrDefault(FILTER, null);

        RoutingDTO routingDTO = RoutingDTO.builder().filterBean(filter)
            .servicePath(defaultPath.get(PATH)).
                serviceTargetUrl(defaultPath.get(URL)).build();

        registerRouterLocator(routeLocator, routingDTO);
      } catch (Exception e) {

        throw new GatewayException(GatewayExceptionCode.GWE003, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    });

    return routeLocator;
  }

  /**
   * 요청 받은 Routing 정보 등록
   *
   * @param routeLocator Routing 관리 객체
   * @throws Exception Exception 오류
   */
  private void registerRouterLocator(Builder routeLocator, RoutingDTO routingDTO)
      throws Exception {

    final boolean hasFilter = !StringUtils.isEmptyParams(routingDTO.getFilterBean());
    final String targetUrl = routingDTO.getServiceTargetUrl();
    final String targetPath = new URL(targetUrl).getPath();
    final String servicePath = routingDTO.getServicePath();
    final String filter = hasFilter ? routingDTO.getFilterBean() : null;
    final int replenishRate = routingDTO.getReplenishRate();
    final int burstCapacity = routingDTO.getBurstCapacity();

    if (hasFilter) {

      GatewayFilter mainFilter = getFilterSpec(filter, routingDTO);

      /*
       최종 Routing 서비스를 G/W에 등록한다.
       Filter 순서는 Body 캐싱용 필터 -> 서비스 API 필터 -> 로깅 필터(Global) 순으로 진행 된다.
      */
      routeLocator.route(r -> r.path(getServicePath(servicePath))
          .filters(f -> f.rewritePath(
              String.format("%s(?<segment>.*)", servicePath),
              String.format("%s${segment}", targetPath))
              .filters(cachingRequestBodyFilter.apply(new CachingRequestBodyFilter.Config()),
                  mainFilter)
              .requestRateLimiter(config -> config
                  .setRateLimiter(new RedisRateLimiter(replenishRate, burstCapacity))))
          .uri(targetUrl));

    } else {

      routeLocator.route(r -> r.path(getServicePath(servicePath))
          .filters(f -> f.rewritePath(
              String.format("%s(?<segment>.*)", servicePath),
              String.format("%s${segment}", targetPath)))
          .uri(targetUrl));
    }
  }

  /**
   * Spring Application Conetext에 등록된 Filter Bean 호출
   *
   * @param filterNm 필터명
   * @param params   Routing 정보
   * @return GatewayFilter 객체
   * @throws Exception 오류
   */
  private GatewayFilter getFilterSpec(String filterNm, RoutingDTO params) throws Exception {

    /*
      ApplicationContext 에 저장 되어 있는 Filter 메소드를 실행한다.
     */
    Object c = appContext.getBean(filterNm);
    Class<?> cl = c.getClass();
    Method method =
        cl.getDeclaredMethod(GatewayCode.GATEWAY_FILTER_APPLY.getCode(), RoutingDTO.class);

    return (GatewayFilter) method.invoke(c, params);
  }

  /**
   * URL Pattern 을 정책에 맞게 수정
   *
   * @param servicePath G/W Router Path
   * @return 최종 Fix Router Path
   */
  public String getServicePath(String servicePath) {

    if ("/".equals(servicePath.substring(servicePath.length() - 1))) {

      servicePath += "**";
    } else {

      servicePath += "/**";
    }

    return servicePath;
  }
}
