package com.kbds.portal.apis.repository.querydsl;

import com.kbds.portal.framework.dto.SearchDTO;
import java.util.List;
import com.kbds.portal.apis.dto.FilterDTO;

/**
 * 
 *
 * <pre>
 *  Class Name     : GwFilterCustomRepository.java
 *  Description    : 필터  QueryDsl 레파지토리
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
public interface GwFilterCustomRepository {

  /**
   * 조건문에 맞는 필터 검색
   * 
   * @param searchDTO
   * @return
   */
  List<FilterDTO> findByConditions(SearchDTO searchDTO);

  /**
   * 필터 등록데이터 중 중복이 불가능한 내용이 DB에 적재 되어 있는지 확인
   * 
   * @param param
   * @return
   */
  boolean isValidData(FilterDTO param);

  /**
   * 필터 수정데이터 중 중복이 불가능한 내용이 DB에 적재 되어 있는지 확인
   * 
   * @param param
   * @return
   */
  boolean checkUpdateValidation(FilterDTO param);

}
