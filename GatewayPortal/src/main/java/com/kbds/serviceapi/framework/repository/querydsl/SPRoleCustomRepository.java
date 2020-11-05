package com.kbds.serviceapi.framework.repository.querydsl;

import com.kbds.serviceapi.framework.dto.RoleDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import java.util.List;

/**
 * <pre>
 *  File  Name     : RoleCustomRepository
 *  Description    : 권한 관련 Querydsl용 Repository
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface SPRoleCustomRepository {

  /**
   * 검색 조건에 따른 권한 테이블 조회
   * @param searchDTO
   * @return
   */
  List<RoleDTO> findByConditions(SearchDTO searchDTO);

}
