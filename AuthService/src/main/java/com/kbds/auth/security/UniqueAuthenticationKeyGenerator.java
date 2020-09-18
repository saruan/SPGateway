package com.kbds.auth.security;

import com.kbds.auth.utils.OAuthUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

/**
 * <pre>
 *  Class Name     : UniqueAuthenticationKeyGenerator.java
 *  Description    : OAuth Access Token 생성 커스텀 클래스
 *                   (Access Token 요청 시마다 새로운 키 발급 - 기본 클래스는 DefaultAuthenticationKeyGenerator)
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-20    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public class UniqueAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

  /**
   * Oauth Token 랜덤 Token으로 복호화
   * @param authentication  인증 객체
   * @return  AccessToken 복호화 값
   */
  public String extractKey(OAuth2Authentication authentication) {

    final String CONST_CLIENT_ID = "client_id";
    final String CONST_SCOPE = "scope";
    final String CONST_USERNAME = "username";
    final String CONST_UUID_KEY = "uuid";

    Map<String, String> values = new LinkedHashMap<>();
    OAuth2Request authorizationRequest = authentication.getOAuth2Request();

    if (!authentication.isClientOnly()) {

      values.put(CONST_USERNAME, authentication.getName());
    }

    values.put(CONST_CLIENT_ID, authorizationRequest.getClientId());

    if (authorizationRequest.getScope() != null) {

      values.put(CONST_SCOPE, OAuth2Utils.formatParameterList(authorizationRequest.getScope()));
    }

    values.put(CONST_UUID_KEY, UUID.randomUUID().toString());

    return OAuthUtils.extractTokenKey(values.toString());
  }
}
