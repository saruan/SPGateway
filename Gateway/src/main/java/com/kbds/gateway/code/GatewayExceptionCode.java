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

  GWE001("사전 검증 실패."),
  GWE002("Invalid Parameters."),
  GWE003("Gateway 기본 Routing 주소 등록 중 오류 발생"),
  GWE004("대상 서버 타임 아웃 발생"),
  GWE005("Filter 동작 실패"),
  GWE006("Invalid API Type"),

  JWT001("Invalid JWT Token."),
  JWT002("JWT Token Validation Failed."),

  TOK001("AccessToken is Expired or Invalid"),
  TOK002("AccessToken 이 유효하지 않습니다."),
  TOK003("API KEY 가 유효하지 않습니다."),
  TOK004("Refresh Token 이 유효하지 않습니다."),

  SAML001("SAML 검증에 실패했습니다."),

  APP001("APP 이 등록되지 않았습니다."),

  HSK001("Invalid HS Key"),

  AUTH001("인증서버 오류 발생"),

  DUMMY("");

  private final String msg;
}
