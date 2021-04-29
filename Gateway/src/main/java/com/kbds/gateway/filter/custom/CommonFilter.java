package com.kbds.gateway.filter.custom;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.code.RoutingCode.ServiceLoginType;
import com.kbds.gateway.dto.RoutingDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.FilterUtils;
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
import org.springframework.validation.annotation.Validated;
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
@Validated
public class CommonFilter extends AbstractGatewayFilterFactory<RoutingDto> {

  private final String checkTokenUrl;
  private final WebClient webClient;

  public CommonFilter(@Value("${services.auth.check-token-url}") String checkTokenUrl,
      WebClient webClient) {

    this.checkTokenUrl = checkTokenUrl;
    this.webClient = webClient;
  }

  @Override
  public GatewayFilter apply(RoutingDto routingDTO) {

    return (exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      try {

        /* 인증 타입, APP Key */
        String appKey = request.getHeaders().getFirst(GrantTypeCode.API_KEY.getCode());
        String hsKey = request.getHeaders().getFirst(GatewayCode.HS_KEY.getCode());

        /* Request Body 추출 */
        Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());

        /* Hs Key 검증 */
        if (attribute instanceof DataBuffer) {

          DataBuffer buffer = (DataBuffer) attribute;
          byte[] requestBody = FilterUtils.getByteArrayFromByteBuffer(buffer.asByteBuffer());

          if (!FilterUtils.isValidHsKey(hsKey, appKey, requestBody)) {

            throw new GatewayException(GatewayExceptionCode.HSK001, HttpStatus.UNAUTHORIZED);
          }
        }

        /* APP KEY 검증 */
        if (!isValidAppKey(routingDTO, appKey)) {

          throw new GatewayException(GatewayExceptionCode.APP001, HttpStatus.UNAUTHORIZED);
        }

        /* Oauth 추가 AccessToken 검증 */
        if (ServiceLoginType.OAUTH.equals(routingDTO.getServiceLoginType())) {

          if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

            throw new GatewayException(GatewayExceptionCode.TOK002, HttpStatus.BAD_REQUEST);
          }

          String accessToken = Objects
              .requireNonNull(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
              .replace(GatewayCode.TOKEN_PREFIX.getCode(), "");

          return checkAccessToken(chain, exchange, accessToken);
        }

        return chain.filter(exchange);
      } catch (GatewayException e) {

        throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()), e.getHttpStatus(),
            e.toString());
      } catch (Exception e) {

        throw new GatewayException(GatewayExceptionCode.GWE005, HttpStatus.BAD_REQUEST,
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
  public boolean isValidAppKey(RoutingDto routingDTO, String appKey) {

    /* Header 에 있는 AppKey 와 해당 Routing URL 이 가지고 있는 Appkey 목록과 비교한다. */
    return routingDTO.getAppKeys().size() > 0 && routingDTO.getAppKeys().contains(appKey);
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
        .retrieve()
        .bodyToMono(Map.class).flatMap(response -> chain.filter(exchange))
        .onErrorMap(error -> new GatewayException(GatewayExceptionCode.TOK001
            , HttpStatus.UNAUTHORIZED, error.getMessage()));
  }
}
