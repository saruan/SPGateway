package com.kbds.gateway.common.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;

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
public class CommonTemplate extends AbstractGatewayFilterFactory<Object> {

  public CommonTemplate() {
    super();
  }

  @Override
  public GatewayFilter apply(Object object) {

    return (exchange, chain) -> {

      // ServerHttpRequest request = exchange.getRequest();
      // String someHeader = request.getHeaders("TEST");

      return chain.filter(exchange);
    };
  }


}
