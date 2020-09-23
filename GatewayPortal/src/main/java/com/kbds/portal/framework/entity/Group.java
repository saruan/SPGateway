package com.kbds.portal.framework.entity;

import com.kbds.portal.apis.entity.AuditLog;
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
 *  File  Name     : Group
 *  Description    : 그룹 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_GROUPS")
public class Group extends AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupId;

  private String groupNm;

  @OneToMany(mappedBy = "group")
  private List<User> users;

  @PrePersist
  public void prePersist() {

    this.setRegUserNo(this.getRegUserNo() == null ? "default" : this.getRegUserNo());
  }
}