package com.kbds.serviceapi.portal.app.dto;

import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 *  File  Name     : AppDetailDTO
 *  Description    : 앱 상세 정보 DTO
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-26          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@Builder
public class AppDetailDTO {

  private Long appId;

  private String appNm;

  private String appKey;

  private String appDesc;

  private List<RoutingDTO> serviceId;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

  @QueryProjection
  public AppDetailDTO(Long appId, String appNm, String appKey, String appDesc,
      List<RoutingDTO> serviceId, String regUserNo, String uptUserNo, Date regDt,
      Date uptDt) {
    this.appId = appId;
    this.appNm = appNm;
    this.appKey = appKey;
    this.appDesc = appDesc;
    this.serviceId = serviceId;
    this.regUserNo = regUserNo;
    this.uptUserNo = uptUserNo;
    this.regDt = regDt;
    this.uptDt = uptDt;
  }
}
