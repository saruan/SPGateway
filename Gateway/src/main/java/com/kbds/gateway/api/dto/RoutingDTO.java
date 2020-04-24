package com.kbds.gateway.api.dto;

import java.util.Date;
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
