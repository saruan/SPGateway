package com.kbds.gateway.bean;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.filter.CachingRequestBodyFilter;
import com.kbds.gateway.utils.StringUtils;
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
 *     변경No        변경일자                변경자          Description
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

  @Autowired
  CachingRequestBodyFilter cachingRequestBodyFilter;

  // Routes,Filter 관리 서버 주소
  @Value("${services.api.route-url}")
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

    Mono<ResponseDTO> responseDTO =
        WebClient.create().get().uri(routeSeverUrl).retrieve().bodyToMono(ResponseDTO.class);

    // Routing 관리 서버로부터 API 목록을 가져온다.
    List<RoutingDTO> routingDTOList = new ObjectMapper().convertValue(
        responseDTO.block().getResultData(), new TypeReference<List<RoutingDTO>>() {});

    // 실제 Routing 서비스, 필터들을 등록한다.
    RouteLocatorBuilder.Builder routeLocator = builder.routes();

    for (RoutingDTO routingDTO : routingDTOList) {

      if (StringUtils.isEmptyParams(routingDTO.getServicePath(),
          routingDTO.getServiceTargetUrl())) {

        continue;
      }

      try {

        String targetPath = new URL(routingDTO.getServiceTargetUrl()).getPath();

        final String servicePath = getServicePath(routingDTO);

        // 필터 유무에 따라 필터 적용을 설정한다.
        if (!StringUtils.isEmptyParams(routingDTO.getFilterBean())) {

          // ApplicationContext에 저장되어 있는 Filter 메소드를 실행한다.
          Object c = appContext.getBean(routingDTO.getFilterBean());
          Class<?> cl = c.getClass();
          Method method =
              cl.getDeclaredMethod(GatewayCode.GATEWAY_FILTER_APPLY.getCode(), Object.class);
          GatewayFilter filter = (GatewayFilter) method.invoke(c, routingDTO);

          // 최종 Routing 서비스를 G/W에 등록한다.
          routeLocator.route(r -> r.path(servicePath)
              .filters(f -> f.rewritePath(routingDTO.getServicePath() + "(?<segment>.*)",
                  targetPath + "${segment}")
                  .filters(cachingRequestBodyFilter.apply(new CachingRequestBodyFilter.Config()),
                      filter))
              .uri(routingDTO.getServiceTargetUrl()));
        } else {

          routeLocator.route(r -> r.path(servicePath)
              .filters(f -> f.rewritePath(routingDTO.getServicePath() + "(?<segment>.*)",
                  targetPath + "${segment}"))
              .uri(routingDTO.getServiceTargetUrl()));
        }

      } catch (Exception e) {

        logger.error(e.toString());
        continue;
      }
    }

    return routeLocator.build();
  }

  /**
   * URL Pattern을 정책에 맞게 수정
   * 
   * @param routingDTO
   * @return
   * @throws MalformedURLException
   */
  public String getServicePath(RoutingDTO routingDTO) throws MalformedURLException {

    String tempServicePath = routingDTO.getServicePath();

    if ("/".equals(tempServicePath.substring(tempServicePath.length() - 1))) {

      tempServicePath += "**";
    } else {

      tempServicePath += "/**";
    }

    return tempServicePath;
  }
}
