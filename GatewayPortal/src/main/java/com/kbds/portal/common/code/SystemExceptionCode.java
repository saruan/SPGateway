package com.kbds.portal.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 *
 * <pre>
 *  Class Name     : SystemExceptionCode.java
 *  Description    : Framework 실패 처리용 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Getter
@AllArgsConstructor
public enum SystemExceptionCode {

  PAR001("PAR001", "요청하신 API의 전문이 잘못 되었습니다. 전문을 확인해 주세요."),
  PAR002("PAR002", "요청하신 API의 주소 정보가 잘못 되었습니다. URI 정보를 확인해 주세요."),

  DUMMY("", "");

  private final String code;
  private final String msg;
}
