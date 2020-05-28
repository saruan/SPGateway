package com.kbds.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;
import com.kbds.gateway.dto.RoutingDTO;

/**
 * 
 *
 * <pre>
 *  Class Name     : SAMLFilter.java
 *  Description    : SAML 발급 필터
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
@Service("SAMLFilter")
public class SAMLFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(SAMLFilter.class);

  public SAMLFilter() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      return chain.filter(exchange);
    };
  }
}
