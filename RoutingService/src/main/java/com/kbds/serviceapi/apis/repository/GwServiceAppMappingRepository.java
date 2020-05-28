package com.kbds.serviceapi.apis.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.apis.entity.GwServiceAppMapping;

/**
 * 
 *
 * <pre>
 *  Class Name     : GwServiceAppMappingRepository.java
 *  Description    : API-APP 매핑 테이블 Repository
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-25    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Repository
public interface GwServiceAppMappingRepository extends CrudRepository<GwServiceAppMapping, Long> {

  /**
   * AppId와 일치하지 않는 데이터 조회
   * 
   * @param appId
   * @return
   */
  public Long deleteByIdAppIdAndIdServiceIdNotIn(Long appId, List<Long> serviceId);

  /**
   * App을 사용중인 데이터 건수 조회
   * 
   * @param appId
   * @return
   */
  public Long countByIdAppId(Long appId);
}
