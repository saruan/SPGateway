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
  COM003("COM003", "파일 타입이 잘못 되었습니다."),

  USR001("USR001", "사용자 정보가 없거나 잘못 되었습니다."),
  USR002("USR002", "이미 등록된 사용자 입니다."),

  GRP001("GRP001", "이미 등록된 그룹입니다."),
  GRP002("GRP002", "사용중인 사용자가 있습니다."),

  SAML001("SAML001", "유효시간이 만료 되었습니다."),
  SAML002("SAML002", "인증서 서명이 잘못 되었습니다."),


  SSL001("SSL001", "인증서 파일이 유효하지 않습니다."),

  CLS001("CLS001", "이미 등록 되어 있는 클러스터입니다."),
  CLS002("CLS002", "메인 클러스터는 반드시 p12 타입을 등록해야 합니다."),

  TOK001("TOK001", "토큰이 만료 되었거나 유효하지 않습니다."),

  DUMMY("", "");

  private String code;
  private String desc;
}
