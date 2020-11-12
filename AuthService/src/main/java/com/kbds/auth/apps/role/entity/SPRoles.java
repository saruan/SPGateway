package com.kbds.auth.apps.role.entity;

import com.kbds.auth.apps.user.entity.SPUsers;
import com.kbds.auth.common.entity.AuditLog;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : Role
 *  Description    : 권한 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_ROLES")
public class SPRoles extends AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roleId;

  private String roleCd;

  private String roleNm;

  @OneToMany(mappedBy = "spRoles")
  private List<SPUsers> spRoles;
}
