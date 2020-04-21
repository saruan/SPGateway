package com.kbds.serviceapi.framework.exception;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.ResponseDTO;

/**
 *
 * <pre>
 *  Class Name     : ServiceExceptionHandler.java
 *  Description    : 공통 Exception 처리 클래스
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
@ControllerAdvice
public class ServiceExceptionHandler {

  Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

  @ExceptionHandler(BizException.class)
  public ResponseEntity<Object> bizExceptionHandler(HttpServletRequest request, BizException ex) {

    ResponseDTO responseDTO = new ResponseDTO();

    // 서비스 레이어에서 던진 코드와 메시지를 결과 값으로 설정한다.
    responseDTO.setResultCode(BizExceptionCode.valueOf(ex.getMessage()).getCode());
    responseDTO.setResultMessage(ex.getArg());

    logger.error(ex.toString());

    return new ResponseEntity<Object>(responseDTO, HttpStatus.OK);
  }
}
