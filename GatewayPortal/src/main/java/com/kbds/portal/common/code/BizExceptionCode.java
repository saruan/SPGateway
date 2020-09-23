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
 *     Ver 1.0      2020-04-16             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Getter
@AllArgsConstructor
public enum BizExceptionCode {

  COM001("COM001", "요청한 서비스의 호출이 실패했습니다. 잠시 후 다시 시도해 주세요."),

  COM002("COM002", "Required parameters is Null"),

  COM003("COM003", "이미 등록된 데이터입니다."),

  COM004("COM004", "존재하지 않는 데이터입니다."),

  COM005("COM005", "유효하지 않은 요청 자료가 있습니다. 파라미터를 체크 해 주세요."),

  COM006("COM006", "APP이 현재 사용 중입니다. 비활성화 후 APP을 삭제해 주세요."),

  COM007("COM007", "Filter가 현재 사용 중입니다. Api 정보를 수정한 후 삭제해 주세요."),

  USR001("USR001", "사용자 정보가 없거나 잘못 되었습니다."),

  DUMMY("", "");

  private final String code;
  private final String msg;
}
