package com.kbds.gateway.filter;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.StringUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
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
 */
@Service("TokenFilter")
public class TokenFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(TokenFilter.class);

  @Value("${oauth.client-id}")
  String oAuthKey;

  public TokenFilter() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      // Request Body 추출
      Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
      DataBuffer buffer = (DataBuffer) attribute;

      // 필수 파라미터 체크
      if (buffer == null) {

        throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED,
            GatewayCode.EMPTY.getCode());
      }

      Map<String, String> queryParam = StringUtils.queryToMap(buffer);

      // SAML 체크
      if (!queryParam.containsKey(GatewayCode.REQUESTPARAM_SAML.getCode())) {

        throw new GatewayException(GatewayExceptionCode.SAM001, HttpStatus.UNAUTHORIZED,
            queryParam.toString());
      }

      // 인증 서버 헤더 변경
      ServerHttpRequest request = exchange.getRequest()
          .mutate()
          .header(HttpHeaders.AUTHORIZATION, oAuthKey)
          .build();

      return chain.filter(exchange.mutate().request(request).build());
    };
  }
}
