package com.kbds.serviceapi.portal.filter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kbds.serviceapi.apis.entity.AuditLog;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *  Class Name     : FilterServiceDTO.java
 *  Description    : 필터링 서비스 빈 관리 DTO
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class FilterDTO extends AuditLog {

  private Long filterId;

  @NotEmpty
  private String filterNm;

  private String filterDesc;

  @NotEmpty
  private String filterBean;

  private String useYn;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

  @QueryProjection
  public FilterDTO(Long filterId, @NotEmpty String filterNm, String filterDesc,
      @NotEmpty String filterBean, String useYn, String regUserNo, String uptUserNo,
      Date regDt, Date uptDt) {
    this.filterId = filterId;
    this.filterNm = filterNm;
    this.filterDesc = filterDesc;
    this.filterBean = filterBean;
    this.useYn = useYn;
    this.regUserNo = regUserNo;
    this.uptUserNo = uptUserNo;
    this.regDt = regDt;
    this.uptDt = uptDt;
  }
}
