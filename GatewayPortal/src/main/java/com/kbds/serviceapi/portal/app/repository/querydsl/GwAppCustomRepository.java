package com.kbds.serviceapi.portal.app.repository.querydsl;

import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.app.entity.GwApp;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  Class Name     : GwAppCustomRepository.java
 *  Description    : App QueryDsl 레파지토리
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public interface GwAppCustomRepository {

  /**
   * 조건문에 맞는 앱 검색
   *
   * @param searchDTO 검색 객체
   * @return  결과 목록
   */
  List<AppDTO> findByConditions(SearchDTO searchDTO);


  /**
   * APP ID로 상세 정보 조회
   * @param appId 앱 ID
   * @return  상세 정보
   */
  Map<GwApp, List<GwService>> findAppDetailById(Long appId);
  
}
