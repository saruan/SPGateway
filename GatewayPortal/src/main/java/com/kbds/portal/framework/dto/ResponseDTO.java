package com.kbds.serviceapi.framework.dto;

import lombok.Data;

/**
 *
 * <pre>
 *  Class Name     : ResponseDTO.java
 *  Description    : 공통 Response 모델 클래스
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
@Data
public class ResponseDTO {

  // 결과 코드
  private String resultCode;

  // 결과 메시지
  private String resultMessage;

  // 결과 데이타
  private Object resultData;

}
