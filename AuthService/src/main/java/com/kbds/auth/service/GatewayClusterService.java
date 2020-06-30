package com.kbds.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kbds.auth.code.AuthCode;
import com.kbds.auth.code.BizExceptionCode;
import com.kbds.auth.entity.GatewayCluster;
import com.kbds.auth.exception.CustomOAuthException;
import com.kbds.auth.repository.GatewayClusterRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  Class Name     : GatewayClusterService.java
 *  Description    : GatewayCluster간 인증 및 키 관리를 수행하는 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-21    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class GatewayClusterService {

  @Autowired
  private GatewayClusterRepository gatewayClusterRespository;

  /**
   * 현재 Gateway에서 인증된 사용자에게 SAML발급을 위해 생성
   *
   * @return
   */
  public String generateSAML() {

    try {

      GatewayCluster gatewayCluster = gatewayClusterRespository.findByMainYn(AuthCode.Y.getCode());

      Algorithm algorithm = Algorithm.HMAC256(gatewayCluster.getSecretKey());

      return JWT.create().withIssuer(gatewayCluster.getGatewayId())
          .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
          .withClaim("Key", UUID.randomUUID().toString()).sign(algorithm);
    } catch (JWTCreationException e) {

      throw new CustomOAuthException(BizExceptionCode.SAML001, e.toString());
    } catch (Exception e) {

      throw new CustomOAuthException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * DB에 등록되어 있는 Cluster의 정보로 SAML 검증
   *
   * @param key
   * @return
   */
  public boolean isValidSAML(String key) {

    List<GatewayCluster> gatewayClusters = null;

    try {

      gatewayClusters = gatewayClusterRespository.findAll();

    } catch (Exception e) {

      throw new CustomOAuthException(BizExceptionCode.COM001, e.toString());
    }

    // 등록 되어 있는 Cluster의 Key값으로 검증 작업 진행
    for (GatewayCluster gatewayCluster : gatewayClusters) {

      try {

        Algorithm algorithm = Algorithm.HMAC256(gatewayCluster.getSecretKey());

        JWT.require(algorithm).withIssuer(gatewayCluster.getGatewayId()).build().verify(key);

        return true;
      } catch (JWTVerificationException e) {

        continue;
      }
    }
    return false;
  }

}
