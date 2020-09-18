package com.kbds.auth.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * <pre>
 *  Class Name     : AuthCode.java
 *  Description    : AuthCode 공통 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-04     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Getter
@AllArgsConstructor
public enum AuthCode {

  // CODE 정보
  SUCCESS("200", "Success"),
  COMMON_FAIL("1000", "Server Fail"),

  Y("Y", "예"),
  N("N", "아니오"),

  // ROLE 정보
  ROLE_ADMIN("ROLE_ADMIN", "관리자 권한"),

  // PARAMETER 정보
  PARAMTER_JWT("jwt", "JWT 파라미터 규격"),

  DUMMY("", "");

  private String code;
  private String desc;
}
