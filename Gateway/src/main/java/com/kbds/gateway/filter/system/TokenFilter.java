package com.kbds.gateway.filter.system;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.dto.RoutingDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.grant.Grant;
import com.kbds.gateway.factory.grant.GrantTypeFactory;
import com.kbds.gateway.utils.StringUtils;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest.Builder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

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
public class TokenFilter extends AbstractGatewayFilterFactory<RoutingDto> {

  private final String oAuthKey;
  private final String username;
  private final String password;
  private final String scope;
  private final GrantTypeFactory grantTypeFactory;

  /**
   * Constructor
   *
   * @param oAuthKey         OAUTH CLIENT ID
   * @param username         대표 계정
   * @param password         대표 계정 비밀번호
   * @param scope            대표 계정 접근 scope
   * @param grantTypeFactory GrantType Validator
   */
  public TokenFilter(@Value("${oauth.client-id}") String oAuthKey,
      @Value("${oauth.username}") String username, @Value("${oauth.password}") String password,
      @Value("${oauth.scope}") String scope, GrantTypeFactory grantTypeFactory) {

    this.oAuthKey = oAuthKey;
    this.scope = scope;
    this.grantTypeFactory = grantTypeFactory;
    this.username = username;
    this.password = password;
  }

  @Override
  public GatewayFilter apply(RoutingDto routingDTO) {

    return (exchange, chain) -> {

      /* Request Body 추출 */
      Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
      DataBuffer buffer = (DataBuffer) attribute;

      /* 필수 파라미터 체크 */
      if (buffer == null) {

        throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED,
            GatewayCode.EMPTY.getCode());
      }

      Map<String, String> queryParam = StringUtils.queryToMap(buffer);

      /* 파라미터 검증 */
      String grantTypeParams = queryParam.get(GrantTypeCode.GRANT_TYPE.getCode());
      Grant grant = grantTypeFactory.makeGrantType(grantTypeParams);

      grant.validateParameters(queryParam);

      Builder builder = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, oAuthKey);

      /* SAML, JWT 는 AccessToken 바로 발급 */
      if (grantTypeParams.equals(GrantTypeCode.JWT.getCode()) ||
          grantTypeParams.equals(GrantTypeCode.SAML.getCode())) {

        LinkedMultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();

        tokenParams.add(GrantTypeCode.USERNAME.getCode(), username);
        tokenParams.add(GrantTypeCode.PASSWORD.getCode(), password);
        tokenParams.add(GrantTypeCode.GRANT_TYPE.getCode(), GrantTypeCode.PASSWORD.getCode());
        tokenParams.add(GrantTypeCode.SCOPE.getCode(), scope);

        /* 인증 서버 헤더 변경 */
        builder.uri(UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
            .replaceQueryParams(tokenParams).build().toUri());
      }

      return chain.filter(exchange.mutate().request(builder.build()).build());
    };
  }
}
