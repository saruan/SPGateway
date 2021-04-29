package com.kbds.gateway.factory.grant.impl;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.grant.Grant;
import com.kbds.gateway.feign.AuthClient;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : SamlGrantType
 *  Description    : SAML TYPE 검증
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-13         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
@AllArgsConstructor
public class SamlGrant implements Grant {

  private final ObjectProvider<AuthClient> authClient;

  @Override
  public void validateParameters(Map<String, String> params) {

    /* 파라미터 검증 */
    if (!params.containsKey(GrantTypeCode.GRANT_TYPE.getCode()) ||
        !params.containsKey(GrantTypeCode.SAML.getCode())) {

      throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED,
          String.valueOf(params));
    }

    String samlAssertion = params.get(GrantTypeCode.SAML.getCode());

    /* 인증 서버에서 SAML 값 검증 */
    ResponseDto responseDTO = Objects.requireNonNull(authClient.getIfAvailable())
        .validateSamlAssertion(samlAssertion);

    /* SAML 값 검증 */
    if (!isValidSamlAssertion(responseDTO)) {

      throw new GatewayException(GatewayExceptionCode.SAML001, HttpStatus.UNAUTHORIZED,
          responseDTO.getResultMessage());
    }
  }

  @Override
  public String getGrantTypeName() {

    return GrantTypeCode.SAML.getCode();
  }

  /**
   * SAML Assertion 검증
   *
   * @param responseDTO 인증서버 통신 결과
   * @return 유효성 체크 결과
   */
  public boolean isValidSamlAssertion(ResponseDto responseDTO) {

    return GatewayCode.SUCCESS_CODE.getCode().equals(responseDTO.getResultCode());
  }
}
