package com.kbds.auth.dto;

import lombok.Data;

/**
 * <pre>
 *  File  Name     : GatewayClusterDTO
 *  Description    : GatewayCluster 정보 DTO 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-18          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
public class GatewayClusterDTO {

  private String gatewayId;
  private String secretKey;
  private String mainYn;
  private Long expiredTime;
}
