package com.kbds.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.kbds.gateway.code.GatewayCode;

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
