package com.kbds.auth.apps.user.entity;

import com.kbds.auth.apps.group.entity.SPGroups;
import com.kbds.auth.apps.role.entity.SPRoles;
import com.kbds.auth.common.entity.AuditLog;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : SPUsers
 *  Description    : 사용자 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25        	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_USERS")
public class SPUsers extends AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String userLoginId;

  private String password;

  private String userNm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "GROUP_ID")
  private SPGroups spGroups;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROLE_ID")
  private SPRoles spRoles;
}
