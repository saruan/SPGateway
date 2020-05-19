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
@Entity
@Table(name = "oauth_refresh_token")
@Data
public class OAuthRefreshToken implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "token_id")
  private String tokenId;

  @Lob
  private byte[] authentication;

  @Lob
  private byte[] token;
}
