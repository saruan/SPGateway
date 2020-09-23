package com.kbds.auth.security.entity;

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
@Data
@Entity(name = "oauth_access_token")
public class OAuthAccessToken implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column
  private String authenticationId;

  @Column
  private String additionalInfo;

  @Lob
  private byte[] authentication;

  @Column
  private String clientId;

  @Column
  private String refreshToken;

  @Lob
  private byte[] token;

  @Column
  private String tokenId;

  @Column
  private String userName;

}
