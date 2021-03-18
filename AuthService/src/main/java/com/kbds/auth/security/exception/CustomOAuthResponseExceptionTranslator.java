package com.kbds.auth.security.exception;

import com.kbds.auth.common.code.BizExceptionCode;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

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
    HttpStatus httpStatus;
    CustomOAuthException customOAuthException;

    if (exception instanceof InvalidTokenException) {

      httpStatus = HttpStatus.UNAUTHORIZED;
      customOAuthException = new CustomOAuthException(BizExceptionCode.TOK001);
    } else {

      OAuth2Exception oAuth2Exception = (OAuth2Exception) responseEntity.getBody();
      httpStatus = responseEntity.getStatusCode();

      customOAuthException = new CustomOAuthException(
          Objects.requireNonNull(oAuth2Exception).getMessage());
    }

    return ResponseEntity.status(httpStatus).body(customOAuthException);
  }
}
