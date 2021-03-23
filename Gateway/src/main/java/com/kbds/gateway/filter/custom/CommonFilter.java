package com.kbds.gateway.filter.custom;

import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

  /* Oauth Client ID */
  @Value("${oauth.client-id}")
  String clientId;

  /* Token Check URL */
  @Value("${gateway.service.check-token.url}")
  String checkTokenUrl;

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      try {

        /* 인증 타입, APP Key */
        String serviceLoginType = routingDTO.getServiceLoginType();
        String appKey = request.getHeaders().getFirst(AuthTypeCode.API_KEY.getCode());

        if (!isValidAppKey(routingDTO, appKey)) {

          throw new GatewayException(GatewayExceptionCode.APP001, HttpStatus.UNAUTHORIZED);
        }

        /* API 가 OAuth Type 일 경우 추가적으로 AccessToken 검증을 수행 한다. */
        if (AuthTypeCode.OAUTH.getCode().equals(serviceLoginType)) {

          if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

            throw new GatewayException(GatewayExceptionCode.TOK002, HttpStatus.BAD_REQUEST);
          }

          String accessToken = Objects
              .requireNonNull(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
              .replace(GatewayCode.TOKEN_PREFIX.getCode(), "");

          /* Oauth 방식일 경우 인증서버에서 토큰 검증 */
          return checkAccessToken(chain, exchange, accessToken);
        }

        return chain.filter(exchange);
      } catch (GatewayException e) {

        throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()), e.getHttpStatus(),
            e.toString());
      }
    };
  }

  /**
   * AppKey 검증
   *
   * @param routingDTO Routing 정보
   * @param appKey     Header AppKey
   * @return AppKey 검증 결과
   */
  public boolean isValidAppKey(RoutingDTO routingDTO, String appKey) {

    /* Header 에 있는 AppKey 와 해당 Routing URL 이 가지고 있는 Appkey 목록과 비교한다. */
    return routingDTO.getAppKeys().size() > 0 && routingDTO.getAppKeys().contains(appKey);
  }

  /**
   * 인증서버에서 Access 토큰 검증
   * @param chain       GatewayFilterChain 객체
   * @param exchange    ServerWebExchange 객체
   * @param accessToken 검증한 AccessToken
   * @return Mono Void
   */
  private Mono<Void> checkAccessToken(GatewayFilterChain chain, ServerWebExchange exchange,
      String accessToken) {

    return WebClient.create().get()
        .uri(checkTokenUrl + "?token={accessToken}", accessToken)
        .retrieve()
        .bodyToMono(Map.class).flatMap(response -> chain.filter(exchange))
        .onErrorMap(
            error -> new GatewayException(GatewayExceptionCode.TOK001,
                HttpStatus.UNAUTHORIZED,
                error.getMessage()));
  }
}
