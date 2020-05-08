package com.kbds.serviceapi.apis.querydsl;

import java.util.List;
import com.kbds.serviceapi.apis.dto.FilterDTO;

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
   * @param param
   * @return
   */
  public List<FilterDTO> findbyConditions(FilterDTO param);

  /**
   * 필터 등록 전 필수 파라미터 중복 체크
   * 
   * @param param
   * @return
   */
  public boolean checkRegistValidation(FilterDTO param);

  /**
   * 필터 수정 전 필수 파라미터 중복 체크
   * 
   * @param param
   * @return
   */
  public boolean checkUpdateValidation(FilterDTO param);

}
