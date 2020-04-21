package com.kbds.gateway.common.bean;

import java.lang.reflect.Method;
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
import com.kbds.gateway.api.model.ResponseDTO;
import com.kbds.gateway.api.model.RoutingDTO;
import com.kbds.gateway.common.constants.CommonConstants;
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

  Logger logger = LoggerFactory.getLogger(RoutingBean.class);

  @Autowired
  ApplicationContext appContext;

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


    WebClient webClient = WebClient.create();

    Mono<ResponseDTO> responseDTO =
        webClient.get().uri(routeSeverUrl).retrieve().bodyToMono(ResponseDTO.class);

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

        // 필터가 있는 경우 해당 필터를 등록해준다.
        if (routingDTO.getFilterBean() != null) {
          
          // filterBean 정보를 불러온 후 등록 되어 있는 Service 정보를 불러온다.
          Object c = appContext.getBean(routingDTO.getFilterBean());
          Class<?> cl = c.getClass();
          final Method method =
              cl.getDeclaredMethod(CommonConstants.GATEWAY_FILTER_APPLY, Object.class);

          // AbstractGatewayFilterFactory를 참조하는 사용자 정의 필터 클래스의 apply 메소드를 호출한다.
          GatewayFilter filter = (GatewayFilter) method.invoke(c, true);

          // Routing URL을 등록한다.
          routeLocator.route(r -> r.path(routingDTO.getServicePath())
              .uri(routingDTO.getServiceTargetUrl()).filters(filter));
        } else {

          // 필터가 없는 경우 필터를 제외한 기본 Routing URL을 등록해준다.
          routeLocator.route(
              r -> r.path(routingDTO.getServicePath()).uri(routingDTO.getServiceTargetUrl()));
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
