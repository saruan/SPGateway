package com.kbds.serviceapi.apis.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
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
import lombok.EqualsAndHashCode;

/**
 * 
 * <pre>
 *  Class Name     : GwService.java
 *  Description    : Gateway 서비스 라우팅 정보를 저장하는 엔티티
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "GW_SERVICE")
public class GwService extends AuditLog implements Serializable {

  private static final long serialVersionUID = 9156878557824834261L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long serviceId;

  @OneToMany(mappedBy = "gwService")
  private List<GwServiceAppMapping> gwApp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FILTER_ID")
  private GwServiceFilter filter;

  private String servicePath;

  private String serviceTargetUrl;

  private String serviceNm;

  private String serviceDesc;

  private String serviceLoginType;

  private String serviceAuthType;

  private String useYn;


  /**
   * 등록 전 Default 데이터 처리
   */
  @PrePersist
  public void prePersist() {

    this.serviceLoginType = this.serviceLoginType == null ? "1" : this.serviceLoginType;
    this.serviceAuthType = this.serviceAuthType == null ? "1" : this.serviceAuthType;
    this.useYn = this.useYn == null ? "Y" : this.useYn;
  }
}
