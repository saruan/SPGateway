package com.kbds.auth.security.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * <pre>
 *  Class Name     : OAuthClientDetail.java
 *  Description    : ClientDetail 테이블 Entity
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-19    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Data
@Entity(name = "oauth_client_details")
public class OAuthClientDetail {

  @Id
  @Column
  private String clientId;

  @Column
  private int accessTokenValidity;

  @Column
  private String additionalInformation;

  private String authorities;

  @Column
  private String authorizedGrantTypes;

  @Column
  private String clientSecret;

  @Column
  private int refreshTokenValidity;

  @Column
  private String resourceIds;

  private String scope;

  @Column
  private String webServerRedirectUri;

}
