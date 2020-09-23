package com.kbds.gateway.factory;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.exception.GatewayException;
import java.util.Map;
import org.springframework.http.HttpStatus;

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
public class RefreshGrantType implements GrantType{

  @Override
  public void validateParameters(Map<String, String> params) {

    if (!params.containsKey(GatewayCode.REFRESH_TOKEN.getCode())) {

      throw new GatewayException(GatewayExceptionCode.TOK004, HttpStatus.UNAUTHORIZED,
          params.toString());
    }
  }
}
