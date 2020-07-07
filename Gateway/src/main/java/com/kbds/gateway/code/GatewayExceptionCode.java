package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  Class Name     : BizExceptionCode.java
 *  Description    : 비지니스 실패 처리용 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */

@Getter
@AllArgsConstructor
public enum GatewayExceptionCode {

  GWE001("GWE001", "사전 검증 실패."),
  GWE002("GWE002", "Invalid Parameters."),
  GWE003("GWE003", "Gateway 기본 Routing 주소 등록 중 오류 발생"),

  SAM001("SAM001", "Invalid SAML."),

  TOK001("TOK001", "AccessToken 만료"),
  TOK002("TOK002", "AccessToken가 유효하지 않습니다."),
  TOK003("TOK003", "API KEY가 유효하지 않습니다."),
  TOK004("TOK004", "Refresh Token이 유효하지 않습니다."),

  APP001("APP001", "APP이 등록되지 않았습니다."),

  AUTH001("AUTH001", "인증서버 오류 발생"),

  DUMMY("", "");

  private String code;
  private String msg;
}
