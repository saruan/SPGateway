package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  File  Name     : GrantTypeCode
 *  Description    : OAuth GrantType
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-12-10         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Getter
@AllArgsConstructor
public enum GrantTypeCode {

  /* Grant Type */
  GRANT_TYPE("grant_type"),
  PASSWORD("password"),
  REFRESH_TOKEN("refresh_token"),
  JWT("jwt"),
  SAML("saml"),

  /* Grant Type Parameter */
  USERNAME("username"),
  SCOPE("scope");

  private final String code;
}
