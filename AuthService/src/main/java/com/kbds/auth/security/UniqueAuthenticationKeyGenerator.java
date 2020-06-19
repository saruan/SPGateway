package com.kbds.auth.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import com.kbds.auth.utils.OAuthUtils;

/**
 *
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
 *
 */
public class UniqueAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(UniqueAuthenticationKeyGenerator.class);

  // 문자열 상수 값
  private final String CLIENT_ID = "client_id";
  private final String SCOPE = "scope";
  private final String USERNAME = "username";
  private final String UUID_KEY = "uuid";

  public String extractKey(OAuth2Authentication authentication) {

    Map<String, String> values = new LinkedHashMap<String, String>();
    OAuth2Request authorizationRequest = authentication.getOAuth2Request();

    if (!authentication.isClientOnly()) {

      values.put(USERNAME, authentication.getName());
    }

    values.put(CLIENT_ID, authorizationRequest.getClientId());

    if (authorizationRequest.getScope() != null) {

      values.put(SCOPE, OAuth2Utils.formatParameterList(authorizationRequest.getScope()));
    }

    String uuid = UUID.randomUUID().toString();

    values.put(UUID_KEY, uuid);

    return OAuthUtils.extractTokenKey(values.toString());
  }
}
