package com.kbds.gateway.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.exception.GatewayException;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kbds.gateway.code.GatewayCode;
import reactor.core.publisher.Mono;

/**
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
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Value("${jwt.secret-key}")
  String secretKey;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {

    String authToken = authentication.getCredentials().toString();
    Authentication auth = getUsernamePasswordAuthentication(authToken);
    SecurityContextHolder.getContext().setAuthentication(auth);

    return Mono.just(auth);
  }

  /**
   * 토큰 값을 베이스로 Security 권한 등록
   *
   * @param token Token 값
   * @return Authentication 객체
   */
  private Authentication getUsernamePasswordAuthentication(String token) {

    if (token != null) {

      try {

        // 토큰 검증
        JWT.require(Algorithm.HMAC256(secretKey))
            .build()
            .verify(token);
      } catch (JWTVerificationException e) {

        throw new GatewayException(GatewayExceptionCode.JWT001, HttpStatus.UNAUTHORIZED);
      } catch (Exception e){

        throw new GatewayException(GatewayExceptionCode.JWT002, HttpStatus.UNAUTHORIZED);
      }

      // Security 권한 등록
      Collection<GrantedAuthority> tmp = new ArrayList<>();
      tmp.add(new SimpleGrantedAuthority(GatewayCode.ROLE_ADMIN.getCode()));

      UserDetails userDetails =
          User.builder().username("user").authorities(tmp).password("pass").build();

      return new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());
    }

    return null;
  }
}
