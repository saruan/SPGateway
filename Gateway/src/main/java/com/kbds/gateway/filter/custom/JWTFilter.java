package com.kbds.gateway.filter.custom;

import com.kbds.gateway.dto.RoutingDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * <pre>
 *  Class Name     : JWTFilter.java
 *  Description    : JWT Token 발급 필터
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Service("JWTFilter")
public class JWTFilter extends AbstractGatewayFilterFactory<RoutingDto> {

  @Override
  public GatewayFilter apply(RoutingDto routingDTO) {

    return (exchange, chain) -> chain.filter(exchange);
  }
}
