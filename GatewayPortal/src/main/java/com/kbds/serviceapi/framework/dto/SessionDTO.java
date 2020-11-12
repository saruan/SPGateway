package com.kbds.serviceapi.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <pre>
 *  File  Name     : SessionDTO
 *  Description    : Front 세션 관리용 dto 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-06          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@AllArgsConstructor
public class SessionDTO {

  private String userNm;
  private String groupNm;
  private String roleCd;
  private String accessToken;
  private String refreshToken;
}
