package com.kbds.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.kbds.gateway.code.GatewayCode;
import reactor.core.publisher.Mono;

/**
 * 
 *
 * <pre>
 *  Class Name     : SecurityContextRepository.java
 *  Description    : Spring Security Authentication 등록
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
public class SecurityContextRepository implements ServerSecurityContextRepository {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {

    return null;
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {

    ServerHttpRequest request = swe.getRequest();

    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    String authToken = null;

    if (authHeader != null && authHeader.startsWith(GatewayCode.TOKEN_PREFIX.getCode())) {

      authToken = authHeader.replace(GatewayCode.TOKEN_PREFIX.getCode(), "");
    }

    if (authToken != null) {

      Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);

      // JWT 토큰을 검증하여 권한 등록
      return this.authenticationManager.authenticate(auth)
          .map((authentication) -> new SecurityContextImpl(authentication));
    } else {

      return Mono.empty();
    }
  }
}
