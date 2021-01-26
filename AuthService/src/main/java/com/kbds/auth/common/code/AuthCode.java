package com.kbds.auth.common.code;

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

  ROLE_SYSTEM("SYSTEM", "시스템 권한"),
  ROLE_ADMIN("ROLE_ADMIN", "관리자 권한"),
  PARAMETERS_JWT("jwt", "JWT 파라미터 규격"),
  DUMMY("", "");

  private final String code;
  private final String desc;
}
