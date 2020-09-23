package com.kbds.serviceapi.framework.config;

import com.kbds.serviceapi.framework.security.CustomAuthenticationProvider;
import com.kbds.serviceapi.framework.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <pre>
 *  File  Name     : SecurityConfiguration
 *  Description    : Spring Security Config 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Configuration
@EnableWebSecurity
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  @Autowired
  RoleService roleService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()
        .antMatchers("/docs/**").permitAll()
        .antMatchers("/api/service/**").permitAll()
        .anyRequest().access("@roleService.hasAuthority(request, authentication)")
        .and()
          .csrf()
            .disable()
          .httpBasic();
  }

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) {

    auth.authenticationProvider(customAuthenticationProvider);
  }
}