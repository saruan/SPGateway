package com.kbds.auth.apps.role.entity;

import com.kbds.auth.apps.menu.entity.SPMenus;
import com.kbds.auth.apps.role.entity.key.SPRoleMenuMappingKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : RoleMenuMapping
 *  Description    :
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
@Entity(name = "SP_ROLE_MENU_MAPPING")
public class SPRoleMenuMapping {

  @EmbeddedId
  SPRoleMenuMappingKey id;

  @ManyToOne
  @JoinColumn(name = "MENU_ID", insertable = false, updatable = false)
  private SPMenus spMenus;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
  private SPRoles spRoles;
}
