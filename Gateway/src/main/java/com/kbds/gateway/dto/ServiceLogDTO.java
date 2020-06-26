package com.kbds.gateway.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLogDTO implements Serializable {

  private static final long serialVersionUID = -297675997652402318L;

  private String requestHeader;
  private String requestParams;
  private String response;
  private String appKey;
  private String serviceNm;
  private String clientService;
  private String requestDt;
  private String responseDt;
}
