package com.kbds.serviceapi.apis.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
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
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RoutingDTO {

  private Long serviceId;

  private Long filterId;

  private String serviceNm;

  private String servicePath;

  private String serviceTargetUrl;

  private String serviceDesc;

  private String serviceLoginType;

  private String serviceAuthType;

  private String useYn;

  private String filterBean;

  private String filterUseYn;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

}
