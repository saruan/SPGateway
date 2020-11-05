package com.kbds.serviceapi.framework.service;

import com.kbds.serviceapi.common.feign.AuthClient;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : UserDetailService
 *  Description    : 사용자 정의 UserDetailService
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPUserService {

  @Autowired
  AuthClient authClient;

  @Value("${services.auth.basic}")
  String basicAuthorization;

  /**
   * Login 처리
   *
   * @param id       사용자 ID
   * @param password 사용자 비밀번호
   * @return  Access Token
   *
   */
  public Map<String, Object> getAccessToken(String id, String password) {

    final String CONST_GRANT_TYPE = "password";
    final String CONST_SCOPE = "read_profile";

    Map<String, String> headers = new HashMap<>();
    headers.put(HttpHeaders.AUTHORIZATION, basicAuthorization);

    return authClient
        .login(headers, id, password, CONST_SCOPE, CONST_GRANT_TYPE);
  }
}
