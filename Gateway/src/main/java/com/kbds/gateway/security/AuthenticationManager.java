package com.kbds.gateway.security;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.kbds.gateway.code.GatewayCode;
import reactor.core.publisher.Mono;

/**
 *
 * <pre>
 *  Class Name     : AuthenticationManager.java
 *  Description    : JWT 토큰 인증 후 Security 권한 등록 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-13    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {

    String authToken = authentication.getCredentials().toString();

    try {

      Authentication auth = getUsernamePasswordAuthentication(authToken);

      SecurityContextHolder.getContext().setAuthentication(auth);

      return Mono.just(auth);

    } catch (Exception e) {
      return Mono.empty();
    }
  }

  /**
   * 토큰 값을 베이스로 Security 권한 등록
   * 
   * @param token
   * @return
   */
  private Authentication getUsernamePasswordAuthentication(String token) {

    if (token != null) {

      Collection<GrantedAuthority> tmp = new ArrayList<>();
      tmp.add(new SimpleGrantedAuthority(GatewayCode.ROLE_ADMIN.getCode()));

      UserDetails userDetails =
          User.builder().username("user").authorities(tmp).password("pass").build();

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      return usernamePasswordAuthenticationToken;
    }

    return null;
  }
}
