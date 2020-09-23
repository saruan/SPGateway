package com.kbds.auth.cluster.repository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.auth.cluster.entity.GatewayCluster;

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

  /**
   * GatewayCluster 목록 조회
   */
  @Override
  @Cacheable(value = "clusterList")
  public List<GatewayCluster> findAll();

  /**
   * GatewayId로 클러스터 조회
   * 
   * @param gatewayId
   * @return
   */
  public GatewayCluster findByGatewayId(String gatewayId);

  /**
   * SecretKey, Id로 클러스터 조회
   * 
   * @param gatewayId
   * @param secretKey
   * @return
   */
  public GatewayCluster findByGatewayIdAndSecretKey(String gatewayId, String secretKey);

  /**
   * 메인 클러스터 조회
   * 
   * @param mainYn
   * @return
   */
  public GatewayCluster findByMainYn(String mainYn);
}
