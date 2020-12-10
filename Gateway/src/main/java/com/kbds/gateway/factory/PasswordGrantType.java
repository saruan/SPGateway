package com.kbds.gateway.factory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.dto.GatewayClusterDTO;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  File  Name     : PasswordGrantType
 *  Description    : PasswordType 체크용 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-20          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
public class PasswordGrantType implements GrantType{

  @Autowired
  ObjectProvider<AuthClient> authClient;

  @Override
  public void validateParameters(Map<String, String> params) {

    if (!(params.containsKey(AuthTypeCode.JWT.getCode()) &&
        isValidToken(selectAllClusters(), params.get(AuthTypeCode.JWT.getCode())))) {

      throw new GatewayException(GatewayExceptionCode.JWT001, HttpStatus.UNAUTHORIZED,
          String.valueOf(params));
    }
  }

  @Override
  public String getGrantTypeName() {

    return GrantTypeCode.PASSWORD.getCode();
  }

  /**
   * JWT 값 검증을 위한 Gateway Cluster Secret 정보 조회 (캐싱 데이터)
   *
   * @return GatewayCluster Secret Key 목록
   */
  @Cacheable(cacheNames = "gatewayClusterList")
  public List<GatewayClusterDTO> selectAllClusters() {

    System.out.println("! " + authClient);

    ResponseDTO responseDTO = Objects.requireNonNull(authClient.getIfAvailable())
        .selectAllClusters();

    return new ObjectMapper().convertValue(
        Objects.requireNonNull(responseDTO).getResultData(),
        new TypeReference<List<GatewayClusterDTO>>() {
        });
  }

  /**
   * Jwt 값 검증
   *
   * @param gatewayClusterDTOS 클러스터 Secret 목록
   */
  public boolean isValidToken(List<GatewayClusterDTO> gatewayClusterDTOS, String jwtToken) {

    /* 등록 되어 있는 Cluster Key 값으로 검증 작업 진행 */
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
