package com.kbds.serviceapi.portal.api.repository;

import com.kbds.serviceapi.portal.api.repository.querydsl.GwRoutingCustomRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.portal.api.entity.GwService;

/**
 * <pre>
 *  Class Name     : GwServiceRepository.java
 *  Description    : 게이트웨이 라우팅 서비스 Repository 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */

@Repository
public interface GwRoutingRepository extends CrudRepository<GwService, Long>,
    GwRoutingCustomRepository {

  /**
   * ServiceId 건수 조회
   *
   * @param serviceId 서비스 ID
   * @return 결과 수
   */
  Long countByServiceIdIn(List<Long> serviceId);

  /**
   * 라우팅 서비스 조회
   *
   * @param serviceId 서비스 ID
   * @return 결과 수
   */
  GwService findByServiceId(Long serviceId);

  /**
   * FilterId 조회
   *
   * @param filterId 필터 ID
   * @return 결과 수
   */
  Long countByFilterFilterId(Long filterId);

  /**
   * 사용중인 APP 개수 조회
   *
   * @param serviceId 서비스 ID
   * @return 결과 수
   */
  Long countByGwAppIdServiceId(Long serviceId);
}
