package com.kbds.auth.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * <pre>
 *  Class Name     : CommonCode.java
 *  Description    : 공통 관리용 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Getter
@AllArgsConstructor
public enum ConstantsCode {

  SUCCESS("200", "Success"),
  COMMON_FAIL("1000", "Server Fail"),

  Y("Y", "예"),
  N("N", "아니오");

  private final String code;
  private final String message;
}
