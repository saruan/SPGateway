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
  COM002("COM002", "유효하지 않은 정보가 포함되어 있습니다."),

  USR001("USR001", "사용자 정보가 없거나 잘못 되었습니다."),
  USR002("USR002", "이미 등록된 사용자 입니다."),

  DUMMY("", "");

  private String code;
  private String desc;
}
