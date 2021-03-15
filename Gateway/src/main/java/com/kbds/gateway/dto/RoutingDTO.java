package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 
 *
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
 *
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutingDTO {

  /* 서비스명 */
  private String serviceNm;
  /* 앱키 목록 */
  private List<String> appKeys;
  /* 최대 Bucket 수 */
  @Builder.Default
  private int replenishRate = 20;
  /* 1초에 요청 가능한 최대 수 */
  @Builder.Default
  private int burstCapacity = 20;
  /* API 경로 */
  private String servicePath;
  /* 목적지 경로 */
  private String serviceTargetUrl;
  /* 서비스 로그인 타입 */
  private String serviceLoginType;
  /* 서비스 인증 타입 */
  private String serviceAuthType;
  /* 필터 종류 */
  private String filterBean;
}
