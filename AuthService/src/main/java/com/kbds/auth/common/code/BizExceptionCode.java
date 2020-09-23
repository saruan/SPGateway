package com.kbds.auth.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  Class Name     : AuthExceptionCode.java
 *  Description    : Exception 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-04     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum BizExceptionCode {

  JWT001("JWT001", "JWT Token 생성 실패"),
  JWT002("JWT002", "JWT Token is not Exist"),
  JWT003("JWT003", "Invalid JWT Token"),

  COM001("COM001", "서비스 처리 실패"),

  DUMMY("", "");

  private String code;
  private String desc;
}
