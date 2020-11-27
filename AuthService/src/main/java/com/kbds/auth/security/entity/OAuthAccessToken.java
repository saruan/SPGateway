package com.kbds.auth.security.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
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
  private String authenticationId;
  private String additionalInfo;
  private byte[] authentication;
  private String clientId;
  private String refreshToken;
  private byte[] token;
  private String tokenId;
  private String userName;
  private Date expiredTime;
}
