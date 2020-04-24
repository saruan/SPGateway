package com.kbds.gateway.common.exception;

import org.springframework.http.HttpStatus;
import com.kbds.gateway.common.code.GatewayExceptionCode;
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
public class GatewayException extends RuntimeException {

  private static final long serialVersionUID = -2310235629517721586L;

  // 사용자 Exception 메시지
  private String arg;

  private HttpStatus httpStatus;

  public GatewayException(GatewayExceptionCode exception, HttpStatus httpStatus, String arg) {

    super(exception.getCode());
    this.httpStatus = httpStatus;
    this.arg = arg;
  }

  public GatewayException(GatewayExceptionCode exception) {

    super(exception.getCode());
  }
}
