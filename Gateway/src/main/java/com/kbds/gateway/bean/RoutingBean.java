package com.kbds.gateway.bean;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.RoutingDTO;
import reactor.core.publisher.Mono;

/**
 * 
 *
 * <pre>
 *  Class Name     : RoutingBean.java
 *  Description    : SPC에서 실제 라우팅 서비스들을 관리하는 Bean을 가진 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
public class RoutingBean {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(RoutingBean.class);

  // Spring Bean을 가져오기 위한 변수
  @Autowired
  ApplicationContext appContext;

  // Routes,Filter 관리 서버 주소
  @Value("${services.api.routeSeverUrl}")
  String routeSeverUrl;

  /**
   * Routing 정보를 조회 한 후 필터와 함께 SPC에 등록하는 메소드
   * 
   * @param builder
   * @return
   */
  @Bean
  @RefreshScope
  public RouteLocator routeLocater(RouteLocatorBuilder builder) {

    // Spring WebClient를 통해 Routing 목록을 가져온다.
    Mono<ResponseDTO> responseDTO =
        WebClient.create().get().uri(routeSeverUrl).retrieve().bodyToMono(ResponseDTO.class);

    // Routing 관리 서버로부터 API 목록을 가져온다.
    List<RoutingDTO> routingDTOList = new ObjectMapper().convertValue(
        responseDTO.block().getResultData(), new TypeReference<List<RoutingDTO>>() {});

    // 실제 Routing 서비스, 필터들을 등록한다.
    RouteLocatorBuilder.Builder routeLocator = builder.routes();

    for (RoutingDTO routingDTO : routingDTOList) {

      // 유효하지 않은 정보가 혹시나 존재한다면 로그만 남기고 등록하지 않는다.
      if (StringUtils.isEmpty(routingDTO.getServicePath())
          || StringUtils.isEmpty(routingDTO.getServiceTargetUrl())) {

        continue;
      }

      try {

        // TODO 정책 확인이 필요.
        // 현재는 /api/sample과 같은 형태로 API 등록을 할 경우 아래의 형태를 모두 수용한다.
        // /api/sample, /api/sample, /api/sample/1
        // 하지만 /api/sample/과 같은 형태로 등록을 할 경우 /api/sample/, /api/sample/1과 같은 형태만 수용

        String targetPath = new URL(routingDTO.getServiceTargetUrl()).getPath();
        String tempServicePath = routingDTO.getServicePath();

        if ("/".equals(tempServicePath.substring(tempServicePath.length() - 1))) {

          tempServicePath += "**";
        } else {

          tempServicePath += "/**";
        }

        final String servicePath = tempServicePath;

        // 필터가 있는 경우 해당 필터를 등록해준다.
        if (routingDTO.getFilterBean() != null) {

          // filterBean 정보를 불러온 후 등록 되어 있는 Service 정보를 불러온다.
          Object c = appContext.getBean(routingDTO.getFilterBean());
          Class<?> cl = c.getClass();
          final Method method =
              cl.getDeclaredMethod(GatewayCode.GATEWAY_FILTER_APPLY.getCode(), Object.class);

          // AbstractGatewayFilterFactory를 참조하는 사용자 정의 필터 클래스의 apply 메소드를 호출한다.
          GatewayFilter filter = (GatewayFilter) method.invoke(c, routingDTO);

          // Routing 정보를 등록한 후 rewritePath를 통해 servicePath 정보를 targetPath로 치환해준다.
          routeLocator
              .route(r -> r.path(servicePath)
                  .filters(f -> f.rewritePath(routingDTO.getServicePath() + "(?<segment>.*)",
                      targetPath + "${segment}").filter(filter))
                  .uri(routingDTO.getServiceTargetUrl()));
        } else {

          // 필터가 없는 경우 필터를 제외한 기본 Routing URL을 등록해준다.
          routeLocator.route(r -> r.path(servicePath)
              .filters(f -> f.rewritePath(routingDTO.getServicePath() + "(?<segment>.*)",
                  targetPath + "${segment}"))
              .uri(routingDTO.getServiceTargetUrl()));
        }

      } catch (Exception e) {

        // 기타 설정 중 문제가 생긴다면 로그만 남기고 다음 서비스 등록으로 넘어간다.
        logger.error(e.toString());
        continue;
      }
    }

    return routeLocator.build();
  }
}
