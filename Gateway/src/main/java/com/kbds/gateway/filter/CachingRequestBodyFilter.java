package com.kbds.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  Class Name     : CachingRequestBodyFilter.java
 *  Description    : RequestBody를 캐시에 담아 다른 Filter에서 사용할 수 있도록 하는 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service("cachingRequestBodyFilter")
public class CachingRequestBodyFilter
    extends AbstractGatewayFilterFactory<CachingRequestBodyFilter.Config> {


  public CachingRequestBodyFilter() {
    super(Config.class);
  }

  public GatewayFilter apply(final Config config) {

    return (exchange, chain) -> ServerWebExchangeUtils.cacheRequestBody(exchange,
        (serverHttpRequest) -> chain.filter(exchange.mutate().request(serverHttpRequest).build()));
  }

  public static class Config {

  }
}
