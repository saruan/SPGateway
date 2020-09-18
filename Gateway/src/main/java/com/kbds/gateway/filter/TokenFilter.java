package com.kbds.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.GatewayClusterDTO;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;
import com.kbds.gateway.utils.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

  @Autowired
  ObjectProvider<AuthClient> authClient;

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
   * Grant_Type, JWT 등 검증
   *
   * @param buffer Request Data
   */
  private void validateParams(DataBuffer buffer) {

    final String CONST_PASSWORD_TYPE = "password";
    final String CONST_JWT = "jwt";
    final String CONST_REFRESH_TOKEN_TYPE = "refresh_token";
    final String CONST_GRANT_TYPE = "grant_type";
    Map<String, String> queryParam = StringUtils.queryToMap(buffer);

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
    // Grant_Type이 password일 경우 JWT Token 체크
    else if (CONST_PASSWORD_TYPE.equals(queryParam.get(CONST_GRANT_TYPE)) && !queryParam
        .containsKey(CONST_JWT)) {

      throw new GatewayException(GatewayExceptionCode.JWT001, HttpStatus.UNAUTHORIZED,
          queryParam.toString());
    }

    // JWT 토큰 검증
    if (!validateJwt(selectAllClusters(), queryParam.get(CONST_JWT))) {

      throw new GatewayException(GatewayExceptionCode.JWT001, HttpStatus.UNAUTHORIZED,
          queryParam.toString());
    }
  }

  /**
   * JWT 값 검증을 위한 Gateway Cluster Secret 정보 조회 (캐싱 데이터)
   *
   * @return GatewayCluster Secret Key 목록
   */
  @Cacheable(cacheNames = "gatewayClusterList")
  public List<GatewayClusterDTO> selectAllClusters() {

    ResponseDTO responseDTO = Objects.requireNonNull(authClient.getIfAvailable())
        .selectAllClusters();

    return new ObjectMapper().convertValue(
        Objects.requireNonNull(responseDTO).getResultData(),
        new TypeReference<List<GatewayClusterDTO>>() {
        });

  }

  /**
   * Jwt값 검증
   *
   * @param gatewayClusterDTOS 클러스터 Secret 목록
   */
  public boolean validateJwt(List<GatewayClusterDTO> gatewayClusterDTOS, String jwtToken) {

    // 등록 되어 있는 Cluster의 Key값으로 검증 작업 진행
    for (GatewayClusterDTO gatewayCluster : gatewayClusterDTOS) {

      try {

        Algorithm algorithm = Algorithm.HMAC256(gatewayCluster.getSecretKey());

        JWT.require(algorithm).withIssuer(gatewayCluster.getGatewayId()).build().verify(jwtToken);

        return true;
      } catch (JWTVerificationException ignore) {
      }
    }
    return false;
  }
}
