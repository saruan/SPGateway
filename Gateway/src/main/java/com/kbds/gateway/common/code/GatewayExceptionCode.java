package com.kbds.gateway.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 *
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
 *
 */

@Getter
@AllArgsConstructor
public enum GatewayExceptionCode {

  GWE0001("GWE0001", "사전 검증 실패. [%s]"),

  DUMMY("", "");

  private String code;
  private String msg;
}
