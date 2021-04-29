package com.kbds.serviceapi.framework.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <pre>
 *  File  Name     : GatewayHeaderAuthenticationFilter
 *  Description    : 포탈 자원서버 인가 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-05        	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    /* 헤더 체크 */
    if (isContainSecurityKey(request)) {

      // Security 권한 등록
      Collection<GrantedAuthority> tmp = new ArrayList<>();
      tmp.add(new SimpleGrantedAuthority(request.getHeader("X_ROLE_NM")));

      UserDetails userDetails =
          User.builder().username("user").authorities(tmp).password("pass").build();

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 헤더에 권한 코드 값이 있는지 체크
   *
   * @param request HttpServletRequest
   * @return 존재 유무
   */
  private boolean isContainSecurityKey(HttpServletRequest request) {

    return request.getHeader("X_ROLE_NM") != null;
  }
}