package com.kbds.auth.apps.api.entity;

import com.kbds.auth.common.entity.AuditLog;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *  File  Name     : SPApi
 *  Description    : Portal API 관리 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-04          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "SP_APIS")
public class SPApis extends AuditLog implements Serializable {

  private static final long serialVersionUID = -1903253310506433690L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long apiId;

  private String apiNm;

  private String apiUrl;

  private String useYn;

  @PrePersist
  public void prePersist() {

    this.setRegUserNo(this.getRegUserNo() == null ? "default" : this.getRegUserNo());
  }
}
