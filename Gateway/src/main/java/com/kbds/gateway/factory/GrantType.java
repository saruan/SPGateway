package com.kbds.gateway.factory;

import java.util.Map;

/**
 * <pre>
 *  File  Name     : GrantType
 *  Description    : Oauth GrantType Interface
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-20         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface GrantType {

  /**
   * GrantType 별 Parameter 체크 메소드
   * @param params  Body 전문
   */
  void validateParameters(Map<String, String> params);

  /**
   * GrantType 이름 리턴
   * @return  GrantType 명
   */
  String getGrantTypeName();
}
