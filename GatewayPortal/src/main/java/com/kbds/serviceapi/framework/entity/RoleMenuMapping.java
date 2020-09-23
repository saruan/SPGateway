package com.kbds.serviceapi.framework.entity;

import com.kbds.serviceapi.framework.entity.key.RoleMenuMappingKey;
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
public class RoleMenuMapping {

  @EmbeddedId
  RoleMenuMappingKey id;

  @ManyToOne
  @JoinColumn(name = "MENU_ID", insertable = false, updatable = false)
  private Menu menu;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
  private Role role;
}
