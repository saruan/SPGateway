package com.kbds.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.kbds.gateway.code.GatewayCode;

/**
 *
 * <pre>
 *  Class Name     : SecurityConfig.java
 *  Description    : Spring Security Main 설정 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-15    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  SecurityContextRepository securityContextRepository;

  @Autowired
  CommonAuthenticationEntryPoint commonAuthenticationEntryPoint;

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {

    //@formatter:off
    //Actuator 관련 API는 인증된 사람만 사용할 수 있도록 설정
    return http.exceptionHandling()
               .authenticationEntryPoint(commonAuthenticationEntryPoint)
               .and()
               .authenticationManager(authenticationManager)
               .securityContextRepository(securityContextRepository)
               .authorizeExchange()
               .pathMatchers("/actuator/**")
                 .hasAuthority(GatewayCode.ROLE_ADMIN.getCode())
               .anyExchange()
                 .permitAll()
               .and()
               .csrf()
                 .disable()
               .build();
    //@formatter:on

  }
}
