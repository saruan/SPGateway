package com.kbds.serviceapi.apis.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.apis.entity.GwApp;

/**
 *
 * <pre>
 *  Class Name     : GwAppRepository.java
 *  Description    : APP 관리 기본 Repisotry
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
public interface GwAppRepository extends CrudRepository<GwApp, Long> {

  public GwApp findByAppId(Long appId);

  public List<GwApp> findByAppNm(String appNm);

  public List<GwApp> findByAppNmAndAppIdNot(String appNm, Long appId);
}
