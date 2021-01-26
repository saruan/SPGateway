package com.kbds.auth.apps.cluster.repository;

import com.kbds.auth.apps.cluster.entity.GatewayCluster;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
  List<GatewayCluster> findAll();

  /**
   * GatewayId로 클러스터 조회
   * 
   * @param gatewayId Gateway Cluster ID
   * @return  GatewayCluster 정보
   */
  GatewayCluster findByGatewayId(String gatewayId);

  /**
   * SecretKey, Id로 클러스터 조회
   * 
   * @param gatewayId Gateway Cluster ID
   * @param secretKey JWT SecretKey
   * @return GatewayCluster 정보
   */
  GatewayCluster findByGatewayIdAndSecretKey(String gatewayId, String secretKey);

  /**
   * 메인 클러스터 조회
   * 
   * @param mainYn  메인 클러스터 여부
   * @return  GatewayCluster 정보
   */
  GatewayCluster findByMainYn(String mainYn);

  /**
   * 게이트웨이 기등록 여부 체크
   * @param gatewayId Gateway Cluster ID
   * @return 등록 되어 있는 숫자
   */
  int countByGatewayId(String gatewayId);
}
