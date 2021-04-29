package com.kbds.gateway.filter.system;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.dto.RoutingDto;
import com.kbds.gateway.dto.TokenDto;
import com.kbds.gateway.exception.GatewayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : SecurityFilter
 *  Description    : Gateway API 인증용 필터
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-23             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service("SecurityFilter")
public class SecurityFilter extends AbstractGatewayFilterFactory<RoutingDto> {

  private final String checkTokenUrl;
  private final WebClient webClient;

  public SecurityFilter(@Value("${services.auth.check-token-url}") String checkTokenUrl,
      WebClient webClient) {

    this.checkTokenUrl = checkTokenUrl;
    this.webClient = webClient;
  }

  @Override
  public GatewayFilter apply(RoutingDto config) {
    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

      if (authorization == null) {

        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.UNAUTHORIZED);
      }

      String accessToken = authorization.replace(GatewayCode.TOKEN_PREFIX.getCode(), "");

      return checkAccessToken(chain, exchange, accessToken);
    };
  }

  /**
   * 인증서버에서 Access 토큰 검증
   *
   * @param chain       GatewayFilterChain 객체
   * @param exchange    ServerWebExchange 객체
   * @param accessToken 검증한 AccessToken
   * @return Mono Void
   */
  public Mono<Void> checkAccessToken(GatewayFilterChain chain, ServerWebExchange exchange,
      String accessToken) {

    return webClient.get()
        .uri(checkTokenUrl + "?token={accessToken}", accessToken)
        .exchange()
        .flatMap(clientResponse -> {

          if (clientResponse.statusCode().isError()) {

            return clientResponse.bodyToMono(ResponseDto.class).flatMap(error -> Mono
                .error(new GatewayException(GatewayExceptionCode.valueOf(error.getResultCode()),
                    clientResponse.statusCode())));
          }

          return clientResponse.bodyToMono(TokenDto.class).flatMap(data -> {

            ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X_ROLE_NM", data.getRoleNm())
                .header("X_USER_ID", data.getUserLoginId())
                .header("X_TOKEN", accessToken)
                .build();

            return chain.filter(exchange.mutate().request(request).build());
          });
        });
  }
}
