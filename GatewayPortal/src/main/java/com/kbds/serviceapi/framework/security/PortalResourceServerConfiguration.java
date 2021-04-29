package com.kbds.serviceapi.framework.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.feign.AuthClient;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.exception.BizException;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
@AllArgsConstructor
@EnableWebSecurity
public class PortalResourceServerConfiguration extends WebSecurityConfigurerAdapter {

  private final AuthClient authClient;
  private final GatewayHeaderAuthenticationFilter filter;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    getAllRoleApiList().forEach((key, value) -> {

      try {

        String[] roleList = value.toArray(new String[0]);
        http.authorizeRequests().antMatchers(key).hasAnyAuthority(roleList);

      } catch (Exception e) {

        throw new BizException(BizExceptionCode.COM001, e.toString());
      }
    });

    /* 기본 경로 설정 */
    http.authorizeRequests()
        .antMatchers("/portal/v1.0/menu/**").permitAll()
        .antMatchers("/portal/v1.0/user/login").permitAll()
        .antMatchers(HttpMethod.POST, "/portal/v1.0/user").permitAll()
        .antMatchers("/api/**").permitAll()
        .antMatchers("/docs/restdoc.html").permitAll()
        .anyRequest().authenticated();

    /* 인가 필터 적용 */
    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * 인증/권한 서버에서 전체 권한으로 관리할 API 목록 조회
   *
   * @return URI, 권한 목록
   */
  public Map<String, List<String>> getAllRoleApiList() {

    ObjectMapper objectMapper = new ObjectMapper();

    return objectMapper
        .convertValue(authClient.selectListForSecurity(CommonUtils.setFeignCommonHeaders())
            .getResultData(), new TypeReference<Map<String, List<String>>>() {
        });
  }
}
