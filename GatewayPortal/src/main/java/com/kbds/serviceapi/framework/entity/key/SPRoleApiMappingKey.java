package com.kbds.serviceapi.framework.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : RoleMenuMappingKey
 *  Description    : 권한-메뉴 매핑 PK
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SPRoleApiMappingKey implements Serializable {

  private static final long serialVersionUID = -711685434231082611L;

  @Column(name = "ROLE_ID")
  private Long roleId;

  @Column(name = "API_ID")
  private Long apiId;

}
