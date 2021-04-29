package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kbds.gateway.code.RoutingCode.ServiceAuthType;
import com.kbds.gateway.code.RoutingCode.ServiceLoginType;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *  Class Name     : RoutingServiceDTO.java
 *  Description    : Routing 서비스 관리용 DTO 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutingDto {

  /* 서비스명 */
  @NotNull
  private String serviceNm;
  /* 앱키 목록 */
  private List<String> appKeys;
  /* API 경로 */
  @NotNull
  private String servicePath;
  /* 목적지 경로 */
  @NotNull
  private String serviceTargetUrl;
  /* 서비스 로그인 타입 */
  @NotNull
  private ServiceLoginType serviceLoginType;
  /* 서비스 인증 타입 */
  @NotNull
  private ServiceAuthType serviceAuthType;
  /* 필터 종류 */
  private String filterBean;
  /* 최대 Bucket 수 */
  @Builder.Default
  private int replenishRate = 20;
  /* 1초에 요청 가능한 최대 수 */
  @Builder.Default
  private int burstCapacity = 20;
  /* 사용자 정의 필터 정보 */
  @Builder.Default
  private List<BlockDto> blockList = new ArrayList<>();
}
