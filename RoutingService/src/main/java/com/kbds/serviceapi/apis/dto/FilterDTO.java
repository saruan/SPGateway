package com.kbds.serviceapi.apis.dto;

import java.util.Date;
import com.kbds.serviceapi.apis.entity.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 *
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
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO extends AuditLog {

  private Long filterId;

  private String filterNm;

  private String filterDesc;

  private String filterBean;

  private String useYn;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;
}
