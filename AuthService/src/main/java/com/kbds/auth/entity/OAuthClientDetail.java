package com.kbds.auth.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "oauth_client_details")
@Entity
public class OAuthClientDetail {

  @Id
  @Column(name = "client_id")
  private String clientId;

  @Column(name = "access_token_validity")
  private int accessTokenValidity;

  @Column(name = "additional_information")
  private String additionalInformation;

  private String authorities;

  @Column(name = "authorized_grant_types")
  private String authorizedGrantTypes;

  private String autoapprove;

  @Column(name = "client_secret")
  private String clientSecret;

  @Column(name = "refresh_token_validity")
  private int refreshTokenValidity;

  @Column(name = "resource_ids")
  private String resourceIds;

  private String scope;

  @Column(name = "web_server_redirect_uri")
  private String webServerRedirectUri;

}
