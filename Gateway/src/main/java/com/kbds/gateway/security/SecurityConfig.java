package com.kbds.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.kbds.gateway.code.GatewayCode;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  SecurityContextRepository securityContextRepository;

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {

    //@formatter:off
    return http .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                      swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                  }))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                      swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                  }))
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/actuator/**")
                  .hasAuthority(GatewayCode.ROLE_ADMIN.getCode())
                .anyExchange()
                  .permitAll()
                .and()
                .csrf().disable()
                .build();
    //@formatter:on

  }
}
