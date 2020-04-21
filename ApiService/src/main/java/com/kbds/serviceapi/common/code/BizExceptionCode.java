package com.kbds.serviceapi.common.code;

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
public enum BizExceptionCode {

  COM001("COM001", "요청한 서비스의 호출이 실패했습니다. [%s]"),

  COM002("COM002", "필수 파라미터 누락. [%s]"),

  COM003("COM003", "이미 등록된 데이터입니다."),

  COM004("COM004", "존재하지 않는 데이터입니다."),

  DUMMY("", "");

  private String code;
  private String msg;
}
