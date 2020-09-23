package com.kbds.portal.framework.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kbds.portal.apis.dto.EmptyDataDTO;
import com.kbds.portal.common.code.BizExceptionCode;
import com.kbds.portal.common.code.SystemExceptionCode;
import com.kbds.portal.framework.dto.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
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
 */
@ControllerAdvice
public class ServiceExceptionHandler {

  Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

  /**
   * BizException 처리용 Handler
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(BizException.class)
  public ResponseEntity<ResponseDTO> bizExceptionHandler(BizException ex) {

    ResponseDTO responseDTO = new ResponseDTO();

    // 서비스 레이어에서 던진 코드와 메시지를 결과 값으로 설정한다.
    responseDTO.setResultCode(BizExceptionCode.valueOf(ex.getMessage()).getCode());
    responseDTO.setResultMessage(BizExceptionCode.valueOf(ex.getMessage()).getMsg());
    responseDTO.setResultData(new EmptyDataDTO());

    // 오류 로그 출력
    if (StringUtils.isEmpty(ex.getMsg())) {

      logger.error(BizExceptionCode.valueOf(ex.getMessage()).getMsg());
    } else {

      logger.error(ex.getMsg());
    }

    return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
  }

  /**
   * RequestBody로 선언되어 있는 DTO 클래스의 변수 타입과 맞지 않는 정보가 왔을 경우 발생하는 오류 핸들러
   *
   * @param ex
   * @return
   */
  @ExceptionHandler({InvalidFormatException.class, MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ResponseDTO> parseExceptionHandler(Exception ex) {

    ResponseDTO responseDTO = new ResponseDTO();

    // 서비스 레이어에서 던진 코드와 메시지를 결과 값으로 설정한다.
    responseDTO.setResultCode(SystemExceptionCode.PAR001.getCode());
    responseDTO.setResultMessage(SystemExceptionCode.PAR001.getMsg());
    responseDTO.setResultData(new EmptyDataDTO());

    // 오류 로그 출력
    logger.error(ex.getMessage());

    return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
  }

  /**
   * DTO 클래스 Validation 오류 핸들러
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDTO> validationExceptionHandler(
      MethodArgumentNotValidException ex) {

    ResponseDTO responseDTO = new ResponseDTO();

    // 서비스 레이어에서 던진 코드와 메시지를 결과 값으로 설정한다.
    responseDTO.setResultCode(BizExceptionCode.COM002.getCode());
    responseDTO.setResultMessage(BizExceptionCode.COM002.getMsg());
    responseDTO.setResultData(new EmptyDataDTO());

    // 오류 로그 출력
    logger.error(ex.getMessage());

    return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
  }
}
