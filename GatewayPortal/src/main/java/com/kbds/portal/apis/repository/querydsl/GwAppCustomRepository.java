package com.kbds.portal.apis.repository.querydsl;

import com.kbds.portal.apis.dto.AppDTO;
import com.kbds.portal.framework.dto.SearchDTO;
import java.util.List;

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
   * @param searchDTO
   * @return
   */
  List<AppDTO> findByConditions(SearchDTO searchDTO);
  
}
