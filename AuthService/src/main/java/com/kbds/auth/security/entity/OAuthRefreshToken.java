package com.kbds.auth.security.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;


/**
 *
 * <pre>
 *  Class Name     : OAuthRefreshToken.java
 *  Description    : RefreshToken Entity
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
@Entity(name = "oauth_refresh_token")
public class OAuthRefreshToken implements Serializable {

  @Id
  private String tokenId;
  private byte[] authentication;
  private byte[] token;
  private Date expiredTime;
}
