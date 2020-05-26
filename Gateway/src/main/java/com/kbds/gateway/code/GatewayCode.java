package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * <pre>
 *  Class Name     : GatewayCode.java
 *  Description    : Gateway 공통 코드
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
public enum GatewayCode {

  Y("Y", "예"),
  N("N", "아니오"),
  ROLE_ADMIN("ROLE_ADMIN", "관리자 권한"),
  GATEWAY_FILTER_APPLY("apply", "Gateway Filter 메소드명"),
  TOKEN_PREFIX("Bearer ", "TOKEN 헤더 PREFIX"),
  API_KEY("api_key", "appKey 헤더 정보"),

  OAUTH_TYPE("1", "OAuth 인증 방식"),

  REUQESTPARAM_SAML("saml", "SAML 토큰"),

  DUMMY("", "");

  private String code;
  private String desc;
}
