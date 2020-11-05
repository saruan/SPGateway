package com.kbds.auth.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import com.kbds.auth.common.code.BizExceptionCode;

/**
 * <pre>
 *  Class Name     : OAuthResponseExceptionTranslator.java
 *  Description    : OAuth Custom Exception Handler
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-21    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public class CustomOAuthResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception exception) throws Exception {

    ResponseEntity responseEntity = super.translate(exception);

    OAuth2Exception oAuth2Exception = (OAuth2Exception) responseEntity.getBody();

    return ResponseEntity.status(responseEntity.getStatusCode())
        .body(new CustomOAuthException(oAuth2Exception.getMessage()));
  }
}
