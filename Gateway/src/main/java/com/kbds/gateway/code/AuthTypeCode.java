package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  File  Name     : AuthTypeCode
 *  Description    : 인증 타입 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-12-10         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@AllArgsConstructor
@Getter
public enum AuthTypeCode {

  OAUTH("OAUTH"),
  JWT("jwt"),
  SAML("saml"),
  API_KEY("api_key");

  private final String code;
}
