package com.kbds.gateway.filter;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;
import com.kbds.gateway.utils.StringUtils;
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

/**
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
 */
@Service("CommonFilter")
public class CommonFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  private final Logger logger = LoggerFactory.getLogger(CommonFilter.class);

  @Autowired
  AuthClient authClient;

  @Value("${oauth.client-id}")
  String clientId;

  private final String TOKEN_VALID_CODE = "active";

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      Map<String, Object> results = null;
      Map<String, String> headers = new HashMap<>();

      // 인증 타입, APP Key
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

        if (!isValidAppKey(routingDTO, appKey)) {

          throw new GatewayException(GatewayExceptionCode.APP001, HttpStatus.UNAUTHORIZED);
        }

        // API가 OAuth Type 일 경우 추가적으로 AccessToken 검증을 수행 한다.
        if (GatewayCode.OAUTH_TYPE.getCode().equals(serviceLoginType)) {

          String accessToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

          checkValidAccessToken(accessToken);
        }
      } catch (GatewayException e) {

        throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()), e.getHttpStatus(),
            requestBody.toString());
      }

      return chain.filter(exchange);
    };
  }

  /**
   * AppKey 검증
   *
   * @param routingDTO Routing 정보
   * @param appKey     Header AppKey
   * @return
   */
  public boolean isValidAppKey(RoutingDTO routingDTO, String appKey) {

    // Header에 있는 AppKey와 해당 Routing URL이 가지고 있는 Appkey 목록과 비교한다.
    if (!StringUtils.isEmptyParams(routingDTO.getAppKeys())) {

      List<String> appKeys = Arrays
          .asList(routingDTO.getAppKeys().split(GatewayCode.APP_KEY_PREFIX.getCode()));

      return !StringUtils.isEmptyParams(appKey) && appKeys.contains(appKey);
    }

    return false;
  }

  /**
   * AccessToken 검증
   *
   * @param accessToken
   */
  public void checkValidAccessToken(String accessToken) {

    Map<String, Object> results;
    Map<String, String> headers = new HashMap<>();

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

  /**
   * AccessToken 유효성 체크
   *
   * @param results
   * @return
   */
  public boolean isValidToken(Map<String, Object> results) {

    return results != null && results.containsKey(TOKEN_VALID_CODE);
  }
}
