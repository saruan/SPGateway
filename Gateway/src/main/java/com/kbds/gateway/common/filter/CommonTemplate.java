package com.kbds.gateway.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;
import com.kbds.gateway.api.dto.RoutingDTO;

/**
 * 
 *
 * <pre>
 *  Class Name     : CommonTemplate.java
 *  Description    : 샘플용 공통 필터 템플릿
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
@Service("CommonTemplate")
public class CommonTemplate extends AbstractGatewayFilterFactory<RoutingDTO> {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(CommonTemplate.class);

  public CommonTemplate() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      String serviceLoginType = routingDTO.getServiceLoginType();

      logger.info("PRE FILTER CALLED [" + serviceLoginType + "]");

      // ServerHttpRequest request = exchange.getRequest();
      // String someHeader = request.getHeaders("TEST");

      if ("1".equals(serviceLoginType)) {

        // throw new GatewayException(GatewayExceptionCode.GWE0001, HttpStatus.UNAUTHORIZED,
        // "Exception test");
      }

      return chain.filter(exchange);
    };
  }
}
