package com.kbds.gateway.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(value = Include.NON_NULL)
public class RoutingDTO {

  private Long serviceId;

  private Long filterId;

  private String serviceNm;

  private String appKeys;

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

  public RoutingDTO(Long serviceId, Long filterId, String serviceNm, String servicePath,
      String serviceTargetUrl, String serviceDesc, String serviceLoginType, String serviceAuthType,
      String useYn, String filterBean, String filterUseYn, String regUserNo, String uptUserNo,
      Date regDt, Date uptDt) {
    super();
    this.serviceId = serviceId;
    this.filterId = filterId;
    this.serviceNm = serviceNm;
    this.servicePath = servicePath;
    this.serviceTargetUrl = serviceTargetUrl;
    this.serviceDesc = serviceDesc;
    this.serviceLoginType = serviceLoginType;
    this.serviceAuthType = serviceAuthType;
    this.useYn = useYn;
    this.filterBean = filterBean;
    this.filterUseYn = filterUseYn;
    this.regUserNo = regUserNo;
    this.uptUserNo = uptUserNo;
    this.regDt = regDt;
    this.uptDt = uptDt;
  }

}
