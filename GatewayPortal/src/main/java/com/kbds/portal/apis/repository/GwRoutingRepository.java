package com.kbds.portal.apis.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.portal.apis.entity.GwService;

/**
 * 
 *
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
 *
 */

@Repository
public interface GwRoutingRepository extends CrudRepository<GwService, Long> {

  /**
   * ServiceId 건수 조회
   * 
   * @param serviceId
   * @return
   */
  public Long countByserviceIdIn(List<Long> serviceId);

  /**
   * 라우팅 서비스 조회
   * 
   * @param serviceId
   * @return
   */
  public GwService findByServiceId(Long serviceId);

  /**
   * FilterId 조회
   * 
   * @param filterId
   * @return
   */
  public Long countByFilterFilterId(Long filterId);
}
