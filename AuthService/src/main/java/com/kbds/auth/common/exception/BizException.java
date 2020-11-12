package com.kbds.auth.common.exception;


import com.kbds.auth.common.code.BizExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * <pre>
 *  Class Name     : BizException.java
 *  Description    : 비지니스 실패용 처리 Exception
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
public class BizException extends RuntimeException {

  private static final long serialVersionUID = -2310235629517721586L;

  // 실제 서버 오류 메시지
  private String msg;

  public BizException(BizExceptionCode exception, String msg) {

    super(exception.getCode());
    this.msg = msg;
  }

  public BizException(BizExceptionCode exception) {

    super(exception.getCode());
  }
}
