package com.kbds.auth.code;

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

  SAML001("SAML001", "SAML 생성 실패"),
  SAML002("SAML002", "SAML값이 없습니다."),
  SAML003("SAML003", "SAML이 유효하지 않습니다."),

  COM001("COM001", "서비스 처리 실패"),

  DUMMY("", "");

  private String code;
  private String desc;
}
