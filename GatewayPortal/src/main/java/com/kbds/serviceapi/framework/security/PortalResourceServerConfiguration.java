package com.kbds.serviceapi.framework.security;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.feign.AuthClient;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.exception.BizException;
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
  AuthClient authClient;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    getAllRoleApiList().forEach((key, value) -> {

      try {

        // 권한 목록을 Spring Expression 에서 사용할 수 있게 'ADMIN', 'USER' 형태로 변환
        String roleList = value.stream()
            .collect(Collectors.joining("','", "'", "'"));

        http.authorizeRequests().antMatchers(key)
            .access("#oauth2.hasScope('read_profile') and hasAnyRole(" + roleList + ")");
      } catch (Exception e) {

        throw new BizException(BizExceptionCode.COM001, e.toString());
      }
    });

    // 기본 필수 경로 설정
    http.authorizeRequests()
        .antMatchers("/portal/v1.0/menu/**").permitAll()
        .antMatchers("/portal/v1.0/user/login").permitAll()
        .antMatchers("/api/**").permitAll()
        .anyRequest().authenticated();
  }

  /**
   * 인증/권한 서버에서 전체 권한으로 관리할 API 목록 조회
   *
   * @return URI, 권한 목록
   */
  public Map<String, List<String>> getAllRoleApiList() {

    return (Map<String, List<String>>) authClient
        .selectListForSecurity(CommonUtils.setFeignCommonHeaders())
        .getResultData();
  }
}
