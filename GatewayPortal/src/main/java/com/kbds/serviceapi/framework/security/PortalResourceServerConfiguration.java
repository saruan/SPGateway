package com.kbds.serviceapi.framework.security;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.framework.repository.querydsl.SPApiCustomRepository;
import com.kbds.serviceapi.framework.service.SPRoleService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * <pre>
 *  File  Name     : PortalResourceServerConfiguration
 *  Description    : 포탈 자원서버 권한 체크 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-05        	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Configuration
@EnableResourceServer
public class PortalResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Autowired
  SPRoleService SPRoleService;

  @Autowired
  SPApiCustomRepository spApiCustomRepository;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    Map<String, List<String>> menuRoleList = spApiCustomRepository.selectListForSecurity();

    menuRoleList.forEach((key, value) -> {

      try {

        String roleList = value.stream()
            .collect(Collectors.joining("','", "'", "'"));

        http.authorizeRequests().antMatchers(key)
            .access("#oauth2.hasScope('read_profile') and hasAnyRole(" + roleList + ")");
      } catch (Exception e) {

        throw new BizException(BizExceptionCode.COM001, e.toString());
      }
    });

    http.authorizeRequests()
        .antMatchers("/portal/v1.0/menu/**").permitAll()
        .antMatchers("/portal/v1.0/user/login").permitAll()
        .antMatchers("/api/**").permitAll()
        .anyRequest().authenticated();
  }
}
