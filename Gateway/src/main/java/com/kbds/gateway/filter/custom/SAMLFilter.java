package com.kbds.gateway.filter.custom;

import com.kbds.gateway.dto.RoutingDTO;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : SAMLFilter
 *  Description    : SAML 발급 필터
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-06         	   구경태          Initialized
 * ------------------------------------------------------------------------------
 *  </pre>
 */
@Service("SAMLFilter")
public class SAMLFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> chain.filter(exchange);
  }
}