package com.kbds.gateway.factory.grant.impl;

import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.grant.Grant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  File  Name     : PasswordGrantType
 *  Description    : PasswordType 체크용 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-20          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
public class PasswordGrant implements Grant {

  @Override
  public void validateParameters(Map<String, String> params) {

    if (!params.containsKey(GrantTypeCode.USERNAME.getCode()) ||
        !params.containsKey(GrantTypeCode.PASSWORD.getCode()) ||
        !params.containsKey(GrantTypeCode.SCOPE.getCode()) ||
        !params.containsKey(GrantTypeCode.GRANT_TYPE.getCode())) {

      throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED,
          String.valueOf(params));
    }
  }

  @Override
  public String getGrantTypeName() {

    return GrantTypeCode.PASSWORD.getCode();
  }
}
