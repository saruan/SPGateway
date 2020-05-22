package com.kbds.gateway.filter;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;

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

  @Autowired
  AuthClient authClient;

  @Value("${oauth.client-id}")
  String clientId;

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(CommonTemplate.class);

  public CommonTemplate() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      String serviceLoginType = routingDTO.getServiceLoginType();

      // OAuth 인증일 경우 인증 서버에서 토큰값이 유효한지 체크한다.
      if (GatewayCode.OAUTH_TYPE.getCode().equals(serviceLoginType)) {

        ServerHttpRequest request = exchange.getRequest();
        String accessToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)
            .replace(GatewayCode.TOKEN_PREFIX.getCode(), "");

        Map<String, String> headers = new HashMap<String, String>();

        headers.put(HttpHeaders.AUTHORIZATION, clientId);
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        Map<String, Object> results = authClient.checkAccessToken(accessToken, headers);

        boolean isValid = isValidToken(results);

        if (!isValid) {

          throw new GatewayException(GatewayExceptionCode.TOK001, HttpStatus.UNAUTHORIZED,
              (String) results.get("code"));
        }
      }

      return chain.filter(exchange);
    };
  }

  /**
   * AccessToken 체크
   * 
   * @param results
   * @return
   */
  public boolean isValidToken(Map<String, Object> results) {

    return results != null && results.containsKey("active");
  }
}
