package com.kbds.serviceapi.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *  Class Name     : AppDTO.java
 *  Description    : App 서비스 관리용 DTO 클래스
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
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class AppDTO {

  private Long appId;

  @NotEmpty
  private String appNm;

  private String appKey;

  private String appDesc;

  private String useYn;

  private List<Long> serviceId;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

  @QueryProjection
  public AppDTO(Long appId, @NotEmpty String appNm, String appKey, String appDesc,
      String useYn, List<Long> serviceId, String regUserNo, String uptUserNo, Date regDt,
      Date uptDt) {

    this.appId = appId;
    this.appNm = appNm;
    this.appKey = appKey;
    this.appDesc = appDesc;
    this.useYn = useYn;
    this.serviceId = serviceId;
    this.regUserNo = regUserNo;
    this.uptUserNo = uptUserNo;
    this.regDt = regDt;
    this.uptDt = uptDt;
  }
}
