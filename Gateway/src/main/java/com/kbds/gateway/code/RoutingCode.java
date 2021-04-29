package com.kbds.gateway.code;

/**
 * <pre>
 *  File  Name     : RoutingCode
 *  Description    : 등록 API 속성 Code
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-21             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class RoutingCode {

  /**
   * 서비스 로그인 타입
   */
  public enum ServiceLoginType{

    OAUTH,
    API_KEY
  }

  /**
   * 서비스 인증 타입
   */
  public enum ServiceAuthType{

    SECURITY,
    PUBLIC
  }
}
