package com.kbds.gateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.filter.CachingRequestBodyFilter;
import com.kbds.gateway.utils.StringUtils;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class RoutingConfiguration {

  private final Logger logger = LoggerFactory.getLogger(RoutingConfiguration.class);
  private ApplicationContext appContext;
  private CachingRequestBodyFilter cachingRequestBodyFilter;
  private ObjectMapper objectMapper;

  // Routes,Filter 관리 서버 주소
  @Value("${services.api.route-url}")
  String routeSeverUrl;

  // Default Routing Info
  private Map<String, Object> service;

  private final String PATH = "path";
  private final String URL = "url";
  private final String FILTER = "filter";

  /**
   * Constructor Injection
   *
   * @param appContext
   * @param cachingRequestBodyFilter
   * @param objectMapper
   */
  @Autowired
  RoutingConfiguration(ApplicationContext appContext,
      CachingRequestBodyFilter cachingRequestBodyFilter,
      ObjectMapper objectMapper) {

    this.appContext = appContext;
    this.cachingRequestBodyFilter = cachingRequestBodyFilter;
    this.objectMapper = objectMapper;
  }

  /**
   * Routing 정보를 조회 한 후 필터와 함께 GATEWAY에 등록 하는 메소드
   *
   * @param builder
   * @return
   */
  @Bean
  @RefreshScope
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {

    RouteLocatorBuilder.Builder routeLocator = builder.routes();
    List<RoutingDTO> routingDTOList;

    try {

      routeLocator = registDefaultService(routeLocator);

      // Routing 관리 서버로부터 API 목록을 가져 온다.
      Mono<ResponseDTO> responseDTO =
          WebClient.create().get().uri(routeSeverUrl).retrieve().bodyToMono(ResponseDTO.class);

      routingDTOList = new ObjectMapper().convertValue(
          responseDTO.block().getResultData(), new TypeReference<List<RoutingDTO>>() {
          });
    } catch (Exception e) {

      logger.error(e.toString());
      return routeLocator.build();
    }

    // 실제 Routing 서비스, 필터 들을 등록 한다.
    for (RoutingDTO routingDTO : routingDTOList) {

      if (StringUtils.isEmptyParams(routingDTO.getServicePath(),
          routingDTO.getServiceTargetUrl())) {

        continue;
      }

      try {

        final boolean hasFilter = !StringUtils.isEmptyParams(routingDTO.getFilterBean());
        final String originUrl = routingDTO.getServiceTargetUrl();
        final String targetPath = new URL(originUrl).getPath();
        final String servicePath = routingDTO.getServicePath();
        final String filter = hasFilter ? routingDTO.getFilterBean() : null;

        registRouterLocator(routeLocator, servicePath, targetPath, originUrl, hasFilter, filter,
            routingDTO);
      } catch (Exception e) {

        logger.error(e.toString());
        continue;
      }
    }

    return routeLocator.build();
  }

  /**
   * Gateway 시스템 기본 필수 Routing 정보 등록
   *
   * @param routeLocator Routing 관리 객체
   * @return
   */
  public Builder registDefaultService(
      Builder routeLocator) {

    service.forEach((key, value) -> {

      try {

        Map<String, String> defaultPath = objectMapper
            .convertValue(value, new TypeReference<Map<String, String>>() {
            });

        final boolean hasFilter = defaultPath.containsKey(FILTER);
        final String servicePath = defaultPath.get(PATH);
        final String originUrl = defaultPath.get(URL);
        final String targetPath = new URL(originUrl).getPath();
        final String filter = hasFilter ? defaultPath.get(FILTER) : null;

        registRouterLocator(routeLocator, servicePath, targetPath, originUrl, hasFilter, filter,
            new RoutingDTO());
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
   * @param servicePath  요청 받은 URL
   * @param targetPath   Endpoint URI (URL Rewrite)
   * @param targetUrl    Endpoint URL
   * @param hasFilter    Filter 존재 유무
   * @param filter       Filter 값 (없으면 null)
   * @return
   * @throws Exception
   */
  private void registRouterLocator(Builder routeLocator, String servicePath,
      String targetPath, String targetUrl, boolean hasFilter, String filter, RoutingDTO routingDTO)
      throws Exception {
    
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
                  mainFilter))
          .uri(targetUrl));

    } else {

      routeLocator.route(r -> r.path(servicePath)
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
   * @return
   * @throws Exception
   */
  private GatewayFilter getFilterSpec(String filterNm, RoutingDTO params) throws Exception {

    // ApplicationContext 에 저장 되어 있는 Filter 메소드를 실행한다.
    Object c = appContext.getBean(filterNm);
    Class<?> cl = c.getClass();
    Method method =
        cl.getDeclaredMethod(GatewayCode.GATEWAY_FILTER_APPLY.getCode(), RoutingDTO.class);
    return (GatewayFilter) method.invoke(c, params);
  }

  /**
   * URL Pattern을 정책에 맞게 수정
   *
   * @return
   * @servicePath Routing 정보
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
