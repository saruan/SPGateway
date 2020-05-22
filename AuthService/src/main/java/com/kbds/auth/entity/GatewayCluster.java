package com.kbds.auth.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * <pre>
 *  Class Name     : GatewayCluster.java
 *  Description    : Gateway 인증 클러스터 테이블
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-21    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Entity
@Table(name = "gateway_cluster")
@Data
public class GatewayCluster implements Serializable {

  private static final long serialVersionUID = 8105593697196965999L;

  @Id
  @Column(name = "gateway_id")
  private String gatewayId;

  @Column(name = "secret_key")
  private String secretKey;

  @Column(name = "main_yn")
  private String mainYn;

  @Column(name = "expired_time")
  private Long expiredTime;

}
