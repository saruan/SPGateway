package com.kbds.serviceapi.apis.querydsl;

import java.util.List;
import com.kbds.serviceapi.apis.dto.AppDTO;

/**
 * 
 *
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
 *
 */
public interface GwAppCustomRepository {

  /**
   * Mapping 테이블 갱신
   * 
   * @param param
   * @return
   */
  public List<AppDTO> updateServiceAppMapping(AppDTO param);


}
