package com.kbds.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
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
import com.kbds.gateway.utils.StringUtils;

/**
 * 
 *
 * <pre>
 *  Class Name     : CommonFilter.java
 *  Description    : 일반 공통 필터 템플릿
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
@Service("CommonFilter")
public class CommonFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  @Autowired
  AuthClient authClient;

  @Value("${oauth.client-id}")
  String clientId;

  // AccessToken 유효 코드
  private final String ACTIVE = "active";

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(CommonFilter.class);

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      Map<String, Object> results = null;
      Map<String, String> headers = new HashMap<String, String>();

      // 인증타입, APP Key
      String serviceLoginType = routingDTO.getServiceLoginType();
      String appKey = request.getHeaders().getFirst(GatewayCode.API_KEY.getCode());

      // Request Body 추출
      Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
      DataBuffer buffer = (DataBuffer) attribute;

      StringBuilder requestBody = new StringBuilder();

      if (buffer != null) {

        requestBody.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
      }

      try {

        // AppKey 검증
        if (!StringUtils.isEmptyParams(routingDTO.getAppKeys())) {

          List<String> appKeys = Arrays.asList(routingDTO.getAppKeys().split(","));

          if (StringUtils.isEmptyParams(appKey) || !appKeys.contains(appKey)) {

            throw new GatewayException(GatewayExceptionCode.TOK003, HttpStatus.UNAUTHORIZED);
          }
        } else {

          throw new GatewayException(GatewayExceptionCode.APP001, HttpStatus.UNAUTHORIZED);
        }

        // OAuth 인증일 경우 인증 서버에서 토큰값이 유효한지 체크한다.
        if (GatewayCode.OAUTH_TYPE.getCode().equals(serviceLoginType)) {

          String accessToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

          if (StringUtils.isEmptyParams(accessToken)) {

            throw new GatewayException(GatewayExceptionCode.TOK001, HttpStatus.UNAUTHORIZED);
          }

          accessToken = accessToken.replace(GatewayCode.TOKEN_PREFIX.getCode(), "");

          headers.put(HttpHeaders.AUTHORIZATION, clientId);
          headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

          try {

            // AuthServer에서 토큰 유효성 체크를 수행한다.
            results = authClient.checkAccessToken(accessToken, headers);

          } catch (Exception e) {

            throw new GatewayException(GatewayExceptionCode.AUTH001, HttpStatus.BAD_REQUEST);
          }

          if (!isValidToken(results)) {

            throw new GatewayException(GatewayExceptionCode.TOK001, HttpStatus.UNAUTHORIZED);
          }
        }
      } catch (GatewayException e) {

        // GatewayException일 경우 입력 파라미터를 Handler에 전달하여 서비스 로그를 저장할 수 있게 한다.
        throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()), e.getHttpStatus(),
            requestBody.toString());
      }

      return chain.filter(exchange);
    };
  }

  /**
   * AccessToken 유효성 체크
   * 
   * @param results
   * @return
   */
  public boolean isValidToken(Map<String, Object> results) {

    return results != null && results.containsKey(ACTIVE);
  }
}
