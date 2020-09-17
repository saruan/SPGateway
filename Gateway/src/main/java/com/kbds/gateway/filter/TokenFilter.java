package com.kbds.gateway.filter;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.StringUtils;
import java.util.Map;
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
 *     Ver 1.0      2020-04-20             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service("TokenFilter")
public class TokenFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  @Value("${oauth.client-id}")
  String oAuthKey;

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

      validateParams(buffer);

      // 인증 서버 헤더 변경
      ServerHttpRequest request = exchange.getRequest()
          .mutate()
          .header(HttpHeaders.AUTHORIZATION, oAuthKey)
          .build();

      return chain.filter(exchange.mutate().request(request).build());
    };
  }

  /**
   * Grant_Type, Saml 등 검증
   *
   * @param buffer
   */
  private void validateParams(DataBuffer buffer) {

    Map<String, String> queryParam = StringUtils.queryToMap(buffer);

    String CONST_PASSWORD_TYPE = "password";
    String CONST_SAML = "saml";
    String CONST_REFRESH_TOKEN_TYPE = "refresh_token";
    String CONST_GRANT_TYPE = "grant_type";

    // Params 체크

    if (!queryParam.containsKey(CONST_GRANT_TYPE)) {

      throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED);
    }

    // Grant_Type이 Refresh Token일 경우 파라미터 사전 체크
    if (CONST_REFRESH_TOKEN_TYPE.equals(queryParam.get(CONST_GRANT_TYPE)) && !queryParam
        .containsKey(CONST_REFRESH_TOKEN_TYPE)) {

      throw new GatewayException(GatewayExceptionCode.TOK004, HttpStatus.UNAUTHORIZED,
          queryParam.toString());
    }
    // Grant_Type이 password일 경우 SAML 체크
    else if (CONST_PASSWORD_TYPE.equals(queryParam.get(CONST_GRANT_TYPE)) && !queryParam
        .containsKey(CONST_SAML)) {

      throw new GatewayException(GatewayExceptionCode.SAM001, HttpStatus.UNAUTHORIZED,
          queryParam.toString());
    }
  }
}
