package com.kbds.auth.apps.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kbds.auth.common.entity.AuditLog;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Entity(name = "SP_MENUS")
public class SPMenus extends AuditLog implements Serializable{

  private static final long serialVersionUID = 2046423416434133418L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  private String menuNm;

  private String menuUrl;

  private String useYn;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_ID")
  private SPMenus parent;

  @JsonIgnore
  @Fetch(FetchMode.SUBSELECT)
  @OneToMany(mappedBy = "parent",  fetch = FetchType.EAGER)
  private Set<SPMenus> child;

  @PrePersist
  public void prePersist() {

    this.setRegUserNo(this.getRegUserNo() == null ? "default" : this.getRegUserNo());
  }

  @Override
  public int hashCode(){

    return this.menuId.hashCode();
  }
}
