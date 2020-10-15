package com.kbds.serviceapi.framework.dto;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <pre>
 *  File  Name     : UserDetails
 *  Description    : 로그인 사용자 정보
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
public class UserDTO implements UserDetails {

  private String username;
  private String password;
  private boolean isEnabled;
  private boolean isAccountNonExpired;
  private boolean isAccountNonLocked;
  private boolean isCredentialsNonExpired;
  private Collection<? extends GrantedAuthority> authorities;
}
