package com.kbds.serviceapi.portal.filter.entity;

import com.kbds.serviceapi.apis.entity.AuditLog;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *
 *  Class Name     : GwServiceFilter.java
 *  Description    : 게이트웨이 라우팅 API의 필터 정보를 저장하는 엔티티
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "GW_SERVICE_FILTER")
public class GwServiceFilter extends AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long filterId;

  private String filterNm;

  private String filterDesc;

  private String filterBean;

  private String useYn;

  /**
   * 등록 전 Default 데이터 처리
   */
  @PrePersist
  public void prePersist() {
    this.useYn = this.useYn == null ? "Y" : this.useYn;
  }


}
