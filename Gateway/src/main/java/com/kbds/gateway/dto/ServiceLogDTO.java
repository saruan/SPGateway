package com.kbds.gateway.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  Class Name     : ServiceLogDTO.java
 *  Description    : 로그 수집용 DTO 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-09    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLogDTO implements Serializable {

  private static final long serialVersionUID = -297675997652402318L;

  /* Trace ID */
  private String tid;
  /* 요청 헤더 */
  private String requestHeader;
  /* 요청 파라미터 */
  private String requestParams;
  /* 응답 전문 */
  private String response;
  /* 전달 메시지 */
  private String message;
  /* 앱키 */
  private String appKey;
  /* 서비스명 */
  private String serviceNm;
  /* 클라이언트 이름 */
  private String clientService;
  /* 요청 시간 */
  private String requestDt;
  /* 응답 시간 */
  private String responseDt;
}
