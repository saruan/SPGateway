package com.kbds.auth.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.auth.entity.GatewayCluster;

/**
 *
 * <pre>
 *  Class Name     : OAuthAccessTokenRepository.java
 *  Description    : 인증 관련 JPA Repository 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-19    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Repository
public interface GatewayClusterRepository extends CrudRepository<GatewayCluster, Long> {

  @Override
  @Cacheable(value = "clusterList")
  List<GatewayCluster> findAll();

  GatewayCluster findByGatewayId(String gatewayId);

  GatewayCluster findByGatewayIdAndSecretKey(String gatewayId, String secretKey);

  GatewayCluster findByMainYn(String mainYn);
}
