package com.kbds.gateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.BlockCode.BlockServlet;
import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.dto.BlockDto.Assertion;
import com.kbds.gateway.dto.BlockDto.ReactiveRest;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.dto.RoutingDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.filter.system.BlockFilter;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter;
import com.kbds.gateway.utils.StringUtils;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class RoutingConfiguration {

  private ApplicationContext appContext;
  private CachingRequestBodyFilter cachingRequestBodyFilter;
  private ObjectMapper objectMapper;
  private BlockFilter blockFilter;
  private WebClient webClient;

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
   * @param webClient                WebClient 객체
   */
  public RoutingConfiguration(ApplicationContext appContext,
      CachingRequestBodyFilter cachingRequestBodyFilter, ObjectMapper objectMapper,
      BlockFilter blockFilter, WebClient webClient) {
    this.appContext = appContext;
    this.cachingRequestBodyFilter = cachingRequestBodyFilter;
    this.objectMapper = objectMapper;
    this.blockFilter = blockFilter;
    this.webClient = webClient;
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
    List<RoutingDto> routingDtoList;

    try {
      /* G/W 시스템 API 등록 */
      registerDefaultService(routeLocator);

      /* 포탈에 등록된 API 목록 조회 */
      routingDtoList = getApiFromPortal();

      /* 사용자 API, 필터 들을 등록 한다. */
      for (RoutingDto routingDTO : routingDtoList) {

        registerRouterLocator(routeLocator, routingDTO);
      }
    } catch (Exception e) {

      log.error(e.toString());
    }

    return routeLocator.build();
  }

  /**
   * Gateway 시스템 기본 필수 Routing 정보 등록
   *
   * @param routeLocator Routing 관리 객체
   */
  private void registerDefaultService(Builder routeLocator) {

    service.forEach((key, value) -> {

      try {

        Map<String, String> defaultPath = objectMapper
            .convertValue(value, new TypeReference<Map<String, String>>() {
            });

        final String filter = defaultPath.getOrDefault(FILTER, null);

        RoutingDto routingDTO = RoutingDto.builder().filterBean(filter)
            .servicePath(defaultPath.get(PATH)).
                serviceTargetUrl(defaultPath.get(URL)).build();

        registerRouterLocator(routeLocator, routingDTO);
      } catch (Exception e) {

        log.error(e.toString());
        throw new GatewayException(GatewayExceptionCode.GWE003, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    });

  }

  /**
   * 요청 받은 Routing 정보 등록
   *
   * @param routeLocator Routing 관리 객체
   * @throws Exception Exception 오류
   */
  public void registerRouterLocator(Builder routeLocator, @Valid RoutingDto routingDTO)
      throws Exception {

    final boolean hasFilter = !StringUtils.isEmptyParams(routingDTO.getFilterBean());
    final String targetUrl = routingDTO.getServiceTargetUrl();
    final String targetPath = new URL(targetUrl).getPath();
    final String servicePath = routingDTO.getServicePath();
    final String filter = hasFilter ? routingDTO.getFilterBean() : null;

    RedisRateLimiter redisRateLimiter = new RedisRateLimiter(20, 20);
    redisRateLimiter.setApplicationContext(appContext);

    /* 유량 제어 등록 */
    redisRateLimiter.getConfig().put(servicePath, new RedisRateLimiter.Config()
        .setBurstCapacity(routingDTO.getBurstCapacity())
        .setReplenishRate(routingDTO.getReplenishRate()));

    if (hasFilter) {

      GatewayFilter mainFilter = getFilterSpec(filter, routingDTO);
      List<BlockDto> blockList = routingDTO.getBlockList();

      /*
       최종 Routing 서비스를 G/W에 등록한다.
       Filter 순서는 Body 캐싱용 필터 -> 서비스 API 필터 -> 로깅 필터(Global) 순으로 진행 된다.
      */
      routeLocator.route(servicePath, r -> r.path(getServicePath(servicePath))
          .filters(f -> f.rewritePath(
              String.format("%s(?<segment>.*)", servicePath),
              String.format("%s${segment}", targetPath))
              .filters(cachingRequestBodyFilter.apply(new CachingRequestBodyFilter.Config()),
                  blockFilter.apply(blockList), mainFilter)
              .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter))
              .circuitBreaker(
                  c -> c.setName("myCircuitBreaker").setFallbackUri("forward:/fallback")))
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
   * Spring Application Context Filter Bean 호출
   *
   * @param filterNm 필터명
   * @param params   Routing 정보
   * @return GatewayFilter 객체
   * @throws Exception 오류
   */
  private GatewayFilter getFilterSpec(String filterNm, RoutingDto params) throws Exception {

    /*
      ApplicationContext 에 저장 되어 있는 Filter 메소드를 실행한다.
     */
    Object c = appContext.getBean(filterNm);
    Class<?> cl = c.getClass();
    Method method =
        cl.getDeclaredMethod(GatewayCode.GATEWAY_FILTER_APPLY.getCode(), RoutingDto.class);

    return (GatewayFilter) method.invoke(c, params);
  }

  /**
   * Portal 서버에서 등록한 모든 API 목록 조회
   *
   * @return API List 객체
   */
  public List<RoutingDto> getApiFromPortal() {

    /* Routing 관리 서버로부터 API 목록을 가져 온다. */
    Mono<ResponseDto> responseDTO =
        webClient.get()
            .uri(routeSeverUrl)
            .retrieve().bodyToMono(ResponseDto.class);

    return new ObjectMapper()
        .convertValue(Objects.requireNonNull(responseDTO.block()).getResultData(),
            new TypeReference<List<RoutingDto>>() {
            });
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

  /**
   * RateLimit 사용을 위한 KeyResolver Bean 등록
   *
   * @return KeyResolver 객체
   */
  @Bean
  public KeyResolver ipKeyResolver() {

    return exchange -> Mono.just(exchange.getRequest().getPath().value());
  }
}
