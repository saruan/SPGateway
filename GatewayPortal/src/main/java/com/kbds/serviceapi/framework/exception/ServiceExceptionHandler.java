package com.kbds.serviceapi.framework.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kbds.serviceapi.apis.dto.EmptyDataDTO;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.code.SystemExceptionCode;
import com.kbds.serviceapi.framework.dto.ResponseDTO;
import feign.FeignException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // 오류 로그 출력
    if (StringUtils.isEmpty(ex.getMsg())) {

      logger.error(BizExceptionCode.valueOf(ex.getMessage()).getMsg());
    } else {

      logger.error(ex.getMsg());
    }

    return new ResponseEntity<>(
        setExceptionObject(BizExceptionCode.valueOf(ex.getMessage()).getCode(),
            BizExceptionCode.valueOf(ex.getMessage()).getMsg()), HttpStatus.BAD_REQUEST);
  }

  /**
   * RequestBody로 선언되어 있는 DTO 클래스의 변수 타입과 맞지 않는 정보가 왔을 경우 발생하는 오류 핸들러
   *
   * @param ex
   * @return
   */
  @ExceptionHandler({InvalidFormatException.class, MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ResponseDTO> parseExceptionHandler(Exception ex) {

    // 오류 로그 출력
    logger.error(ex.getMessage());

    return new ResponseEntity<>(
        setExceptionObject(SystemExceptionCode.PAR001.getCode(),
            SystemExceptionCode.PAR001.getMsg()), HttpStatus.BAD_REQUEST);
  }

  /**
   * DTO 클래스 Validation 오류 핸들러
   *
   * @param ex
   * @return
   */
  @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ResponseDTO> validationExceptionHandler(
      Exception ex) {

    // 오류 로그 출력
    logger.error(ex.getMessage());

    return new ResponseEntity<>(
        setExceptionObject(BizExceptionCode.COM005.getCode(),
            BizExceptionCode.COM005.getMsg()), HttpStatus.OK);
  }

  /**
   * FeignClient Exception Handler
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ResponseDTO> feignClientExceptionHandler(
      FeignException ex) throws JsonProcessingException {

    // 오류 로그 출력
    logger.error(ex.getMessage());

    ObjectMapper mapper = new ObjectMapper();

    ResponseDTO responseDTO = mapper.readValue(ex.contentUTF8(), ResponseDTO.class);

    return new ResponseEntity<>(responseDTO, HttpStatus.resolve(ex.status()));
  }

  /**
   * ResponseDTO 오류 객체 생성
   *
   * @param code 오류 코드
   * @param msg  오류 메시지
   * @return ResponseDTO 객체
   */
  private ResponseDTO setExceptionObject(String code, String msg) {

    ResponseDTO responseDTO = new ResponseDTO();

    // 서비스 레이어에서 던진 코드와 메시지를 결과 값으로 설정한다.
    responseDTO.setResultCode(code);
    responseDTO.setResultMessage(msg);
    responseDTO.setResultData(new EmptyDataDTO());

    return responseDTO;
  }
}
