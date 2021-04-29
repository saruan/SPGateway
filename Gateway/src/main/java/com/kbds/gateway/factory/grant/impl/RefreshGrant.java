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
 *  File  Name     : RefreshGrantType
 *  Description    : Refresh Token 발급 체크용 클래스
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
public class RefreshGrant implements Grant {

  @Override
  public void validateParameters(Map<String, String> params) {

    if (!params.containsKey(GrantTypeCode.REFRESH_TOKEN.getCode())) {

      throw new GatewayException(GatewayExceptionCode.TOK004, HttpStatus.UNAUTHORIZED,
          params.toString());
    }
  }

  @Override
  public String getGrantTypeName() {

    return GrantTypeCode.REFRESH_TOKEN.getCode();
  }
}
