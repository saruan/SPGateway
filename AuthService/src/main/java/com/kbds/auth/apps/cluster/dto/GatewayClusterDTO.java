package com.kbds.auth.apps.cluster.dto;

import java.util.Base64;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
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

  @NotEmpty
  private String gatewayId;
  private String secretKey = new String(
      Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
  private String mainYn;
  private Long expiredTime = 3600L;
  private byte[] certificateFile;
  private String certificatePassword;
}
