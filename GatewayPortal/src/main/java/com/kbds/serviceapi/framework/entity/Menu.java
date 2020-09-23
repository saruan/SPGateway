package com.kbds.serviceapi.framework.entity;

import com.kbds.serviceapi.apis.entity.AuditLog;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : Menu
 *  Description    : 메뉴 관리 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-26          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_MENUS")
public class Menu extends AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  private String menuNm;

  private String menuUrl;

  @OneToMany(mappedBy = "role")
  private List<RoleMenuMapping> roleMenuMappings;

  @PrePersist
  public void prePersist() {

    this.setRegUserNo(this.getRegUserNo() == null ? "default" : this.getRegUserNo());
  }
}
