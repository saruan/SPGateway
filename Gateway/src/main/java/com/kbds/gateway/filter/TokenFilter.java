package com.kbds.gateway.filter;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.StringUtils;

/**
 * 
 *
 * <pre>
 *  Class Name     : TokenFilter.java
 *  Description    : AccessToken 발급용 필터
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
@Service("TokenFilter")
public class TokenFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(TokenFilter.class);

  public TokenFilter() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      // Webflux는 HttpServerRequest에서 body를 한번만 읽을 수 있어 Cache에 담아둔다.
      Object attribute = exchange.getAttribute("cachedRequestBody");

      // DataBuffer Type 체크
      if (attribute instanceof DataBuffer) {

        DataBuffer buffer = (DataBuffer) attribute;

        Map<String, String> queryParam = StringUtils.queryToMap(buffer);

        // SAML 체크
        if (!queryParam.containsKey(GatewayCode.REUQESTPARAM_SAML.getCode())) {

          throw new GatewayException(GatewayExceptionCode.GWE0001, HttpStatus.UNAUTHORIZED,
              "Invalid SAML");
        }

      } else {

        throw new GatewayException(GatewayExceptionCode.GWE0001, HttpStatus.UNAUTHORIZED,
            "Invalid Paramters");
      }

      return chain.filter(exchange);
    };
  }
}
