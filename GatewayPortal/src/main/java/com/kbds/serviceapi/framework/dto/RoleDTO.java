package com.kbds.serviceapi.framework.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * <pre>
 *  File  Name     : RoleDTO
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@Builder
public class RoleDTO {

  private Long roleId;

  @NonNull
  private String roleCd;

  @NonNull
  private String roleNm;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;
}
