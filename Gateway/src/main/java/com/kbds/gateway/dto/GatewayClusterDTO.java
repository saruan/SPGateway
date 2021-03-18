package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Base64;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : GatewayClusterDTO
 *  Description    : 인증서버 GatewayCluster DTO 클래스
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
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class GatewayClusterDTO {

  /* 게이트웨이 클러스터 ID */
  @NotEmpty
  private String gatewayId;
  /* 게이트웨이 비밀키 */
  private String secretKey;
  /* 메인 클러스터 유무 */
  private String mainYn;
  /* 토큰 만료 시간 */
  private Long expiredTime;
  /* 인증서 파일 */
  private byte[] certificateFile;
  /* 인증서 비밀번호 */
  private String certificatePassword;
}
