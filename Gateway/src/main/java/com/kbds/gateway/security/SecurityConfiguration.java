package com.kbds.gateway.security;

import com.kbds.gateway.code.GatewayCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * <pre>
 *  Class Name     : SecurityConfiguration.java
 *  Description    : Spring Security Main 설정 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-15    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final CommonAuthenticationEntryPoint commonAuthenticationEntryPoint;

  /**
   * Constructor Injections
   *
   * @param authenticationManager          AuthenticationManager 객체
   * @param securityContextRepository      SecurityContextRepository 객체
   * @param commonAuthenticationEntryPoint CommonAuthenticationEntryPoint 객체
   */
  public SecurityConfiguration(
      AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository,
      CommonAuthenticationEntryPoint commonAuthenticationEntryPoint) {

    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.commonAuthenticationEntryPoint = commonAuthenticationEntryPoint;
  }

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {

    return http.exceptionHandling()
        .authenticationEntryPoint(commonAuthenticationEntryPoint)
        .and()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers("/actuator/**")
        .hasAuthority(GatewayCode.ROLE_ADMIN.getCode())
        .pathMatchers("/gateway/**")
        .hasAnyAuthority(GatewayCode.ROLE_ADMIN.getCode())
        .anyExchange()
        .permitAll()
        .and()
        .csrf()
        .disable()
        .build();
  }
}
