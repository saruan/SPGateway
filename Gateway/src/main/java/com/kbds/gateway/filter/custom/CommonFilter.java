package com.kbds.gateway.filter.custom;

import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;
import com.kbds.gateway.utils.StringUtils;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

  @Autowired
  AuthClient authClient;

  @Value("${oauth.client-id}")
  String clientId;

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      /* 인증 타입, APP Key */
      String serviceLoginType = routingDTO.getServiceLoginType();
      String appKey = request.getHeaders().getFirst(AuthTypeCode.API_KEY.getCode());

      /* Request Body 추출 */
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

        /* API 가 OAuth Type 일 경우 추가적으로 AccessToken 검증을 수행 한다. */
        if (AuthTypeCode.OAUTH.getCode().equals(serviceLoginType)) {

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
   * @return  AppKey 검증 결과
   */
  public boolean isValidAppKey(RoutingDTO routingDTO, String appKey) {

    /* Header 에 있는 AppKey 와 해당 Routing URL 이 가지고 있는 Appkey 목록과 비교한다. */
    return routingDTO.getAppKeys().size() > 0 && routingDTO.getAppKeys().contains(appKey);
  }

  /**
   * AccessToken 검증
   *
   * @param accessToken 토큰값
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

      /* AuthServer 에서 토큰 유효성 체크를 수행한다. */
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
   * @param results Token 검증 결과
   * @return  유효성 검증 결과
   */
  public boolean isValidToken(Map<String, Object> results) {

    return results != null && results.containsKey("active");
  }
}
