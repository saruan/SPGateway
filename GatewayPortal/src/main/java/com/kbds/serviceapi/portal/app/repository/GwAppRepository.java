package com.kbds.serviceapi.portal.app.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.portal.app.entity.GwApp;

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

  /**
   * APP ID로 단건 검색
   * 
   * @param appId 앱 ID
   * @return  검색 결과
   */
  GwApp findByAppId(Long appId);

  /**
   * APP 이름으로 검색
   * 
   * @param appNm 앱 이름
   * @return  검색 결과
   */
  List<GwApp> findByAppNm(String appNm);

  /**
   * 변경하려는 APP 이름이 DB상에서 사용중인지 검색
   * 
   * @param appNm 앱 이름
   * @param appId 앱 ID
   * @return  중복 여부
   */
  List<GwApp> findByAppNmAndAppIdNot(String appNm, Long appId);
}
