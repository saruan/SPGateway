package com.kbds.auth.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 * 
 *
 * <pre>
 *  Class Name     : OAuthUtils.java
 *  Description    : OAuth 관련 공통 유틸 클래스
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
public class OAuthUtils {

  /**
   * Token MD5 복호화
   * 
   * @param value
   * @return
   */
  public static String extractTokenKey(String value) {

    if (value == null) {

      return null;
    } else {

      MessageDigest digest;

      try {

        digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      } catch (NoSuchAlgorithmException var5) {

        throw new IllegalStateException(
            "MD5 algorithm not available.  Fatal (should be in the JDK).");
      }

      try {

        byte[] e = digest.digest(value.getBytes(StandardCharsets.UTF_8.name()));
        return String.format("%032x", new Object[] {new BigInteger(1, e)});
      } catch (UnsupportedEncodingException var4) {

        throw new IllegalStateException(
            "UTF-8 encoding not available.  Fatal (should be in the JDK).");
      }
    }
  }

  /**
   * Oauth Token 랜덤 Token으로 복호화
   * @param authentication  인증 객체
   * @return  AccessToken 복호화 값
   */
  public static String extractKey(OAuth2Authentication authentication) {

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
