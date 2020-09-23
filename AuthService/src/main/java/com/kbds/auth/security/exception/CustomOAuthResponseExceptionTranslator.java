package com.kbds.auth.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import com.kbds.auth.common.code.BizExceptionCode;

/**
 *
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
 *
 */
public class CustomOAuthResponseExceptionTranslator
    implements WebResponseExceptionTranslator<OAuth2Exception> {

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception exception){

    if (exception instanceof CustomOAuthException) {

      OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;

      return ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
          .body(new CustomOAuthException(BizExceptionCode.valueOf(oAuth2Exception.getMessage()),
              oAuth2Exception.getOAuth2ErrorCode()));

    } else if (exception instanceof AuthenticationException) {

      AuthenticationException authenticationException = (AuthenticationException) exception;

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new CustomOAuthException(authenticationException.getMessage()));
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(new CustomOAuthException(exception.getMessage()));
  }
}
