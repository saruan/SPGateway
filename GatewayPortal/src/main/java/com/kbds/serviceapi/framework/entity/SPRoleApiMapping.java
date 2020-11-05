package com.kbds.serviceapi.framework.entity;

import com.kbds.serviceapi.framework.entity.key.SPRoleApiMappingKey;
import com.kbds.serviceapi.framework.entity.key.SPRoleMenuMappingKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : SPRoleApiMapping
 *  Description    : 권한-API 매핑 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_ROLE_API_MAPPING")
public class SPRoleApiMapping {

  @EmbeddedId
  SPRoleApiMappingKey id;

  @ManyToOne
  @JoinColumn(name = "API_ID", insertable = false, updatable = false)
  private SPApis spApis;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
  private SPRoles spRoles;
}
