package com.kbds.serviceapi.apis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.apis.entity.GwService;

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

}
