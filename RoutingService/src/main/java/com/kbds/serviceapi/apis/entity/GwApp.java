package com.kbds.serviceapi.apis.entity;

import java.io.Serializable;
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
 *  Class Name     : GWApp.java
 *  Description    : Gateway APP 관리 정보를 저장하는 엔티티
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-25    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "GW_APP")
public class GwApp extends AuditLog implements Serializable {

  private static final long serialVersionUID = -5261310390196651377L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long appId;

  @OneToMany(mappedBy = "gwApp")
  private List<GwServiceAppMapping> gwService;

  private String appNm;

  private String appKey;

  private String appDesc;

  private String useYn;

  /**
   * 등록 전 Default 데이터 처리
   */
  @PrePersist
  public void prePersist() {

    this.useYn = this.useYn == null ? "Y" : this.useYn;
  }
}
