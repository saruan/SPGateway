package com.kbds.auth.apps.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kbds.auth.apps.cluster.entity.GatewayCluster;
import com.kbds.auth.apps.cluster.repository.GatewayClusterRepository;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.code.ConstantsCode;
import com.kbds.auth.security.exception.CustomOAuthException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : JwtService
 *  Description    : JWT 토큰 발급 관련 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-20             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class JwtService {

  private final GatewayClusterRepository gatewayClusterRepository;

  public JwtService(GatewayClusterRepository gatewayClusterRepository) {
    this.gatewayClusterRepository = gatewayClusterRepository;
  }

  /**
   * 현재 Gateway 에서 인증된 사용자에게 JWT 발급을 위해 생성
   *
   * @return 생성된 JWT 키값
   */
  public String generateJWT() {

    try {

      GatewayCluster gatewayCluster = gatewayClusterRepository
          .findByMainYn(ConstantsCode.Y.name());

      Algorithm algorithm = Algorithm.HMAC256(gatewayCluster.getSecretKey());

      return JWT.create().withIssuer(gatewayCluster.getGatewayId())
          .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
          .withClaim("Key", UUID.randomUUID().toString()).sign(algorithm);
    } catch (JWTCreationException e) {

      throw new CustomOAuthException(BizExceptionCode.JWT001, e.toString());
    } catch (Exception e) {

      throw new CustomOAuthException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * DB에 등록되어 있는 Cluster의 정보로 JWT 토큰 검증
   *
   * @param key JWT Key값
   * @return 유효성 결과
   */
  public boolean isValidJWT(String key) {

    List<GatewayCluster> gatewayClusters;

    try {

      gatewayClusters = gatewayClusterRepository.findAll();

    } catch (Exception e) {

      throw new CustomOAuthException(BizExceptionCode.COM001, e.toString());
    }

    for (GatewayCluster gatewayCluster : gatewayClusters) {

      try {

        Algorithm algorithm = Algorithm.HMAC256(gatewayCluster.getSecretKey());

        JWT.require(algorithm).withIssuer(gatewayCluster.getGatewayId()).build().verify(key);

        return true;
      } catch (JWTVerificationException ignore) {
      }
    }
    return false;
  }

}
