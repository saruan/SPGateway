package com.kbds.auth.security;

import com.kbds.auth.security.OAuth2RefreshConfig.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <pre>
 *  Class Name     : SecurityConfig.java
 *  Description    : Spring Security 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-20    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable().authorizeRequests().anyRequest().permitAll();
  }

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication().withUser("user").password("{noop}pass").roles("USER");
    customUserDetailsService.addService(auth.getDefaultUserDetailsService());
  }

  @Bean
  public PasswordEncoder encoder() {

    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {

    return super.authenticationManager();
  }
}
