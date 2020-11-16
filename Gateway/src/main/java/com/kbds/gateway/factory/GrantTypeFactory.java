package com.kbds.gateway.factory;

import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.exception.GatewayException;
import org.springframework.http.HttpStatus;

/**
 * <pre>
 *  File  Name     : GrantTypeFactory
 *  Description    : GrantType Factory 객체
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-20          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class GrantTypeFactory {

  final String CONST_PASSWORD_TYPE = "password";
  final String CONST_REFRESH_TOKEN_TYPE = "refresh_token";

  /**
   * GrantType 에 따라 생성자를 다르게 전달해준다.
   *
   * @param grantType Oauth GrantType
   * @return GrantType 객체
   */
  public GrantType makeGrantType(String grantType) {

    switch (grantType) {
      case CONST_PASSWORD_TYPE:
        return new PasswordGrantType();
      case CONST_REFRESH_TOKEN_TYPE:
        return new RefreshGrantType();
      default:
        throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED);
    }
  }
}
