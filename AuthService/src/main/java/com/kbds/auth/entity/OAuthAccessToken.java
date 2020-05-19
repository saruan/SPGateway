package com.kbds.auth.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;


/**
 *
 * <pre>
 *  Class Name     : OAuthAccessToken.java
 *  Description    : AccessToken Entity
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
@Entity
@Table(name = "oauth_access_token")
@Data
public class OAuthAccessToken implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "authentication_id")
  private String authenticationId;

  @Column(name = "additional_info")
  private String additionalInfo;

  @Lob
  private byte[] authentication;

  @Column(name = "client_id")
  private String clientId;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Lob
  private byte[] token;

  @Column(name = "token_id")
  private String tokenId;

  @Column(name = "user_name")
  private String userName;

}
