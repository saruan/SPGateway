package com.kbds.auth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kbds.auth.code.BizExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * <pre>
 *  Class Name     : GatewayException.java
 *  Description    : Gateway 실패용 커스텀 Exception
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(using = CustomOAuthExceptionSerializer.class)
public class CustomOAuthException extends OAuth2Exception {

  private static final long serialVersionUID = -2310235629517721586L;

  // 사용자 Exception 메시지
  private String arg;

  public CustomOAuthException(BizExceptionCode exception, String arg) {

    super(exception.getCode());
    this.arg = arg;
  }

  public CustomOAuthException(String msg) {

    super(msg);
  }

  public CustomOAuthException(BizExceptionCode exception) {

    super(exception.getCode());
  }
}
