package com.kbds.gateway.factory.granttype;

import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.code.GrantTypeCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.feign.AuthClient;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.Gateway;
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
public class SamlGrantType implements GrantType {

  private final ObjectProvider<AuthClient> authClient;

  @Override
  public void validateParameters(Map<String, String> params) {

    /* 파라미터 검증 */
    if (!params.containsKey(GrantTypeCode.GRANT_TYPE.getCode()) ||
        !params.containsKey(AuthTypeCode.SAML.getCode())) {

      throw new GatewayException(GatewayExceptionCode.GWE002, HttpStatus.UNAUTHORIZED,
          String.valueOf(params));
    }

    String samlAssertion = params.get(AuthTypeCode.SAML.getCode());

    /* 인증 서버에서 SAML 값 검증 */
    ResponseDTO responseDTO = authClient.getIfAvailable().validateSamlAssertion(samlAssertion);

    /* SAML 값 검증 */
    if(!isValidSamlAssertion(responseDTO)){

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
  public boolean isValidSamlAssertion(ResponseDTO responseDTO){

    return GatewayCode.SUCCESS_CODE.getCode().equals(responseDTO.getResultCode());
  }
}
