package com.kbds.gateway.exception;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.utils.StringUtils;
import reactor.core.publisher.Mono;

/**
 *
 * <pre>
 *  Class Name     : GlobalErrorWebExceptionHandler.java
 *  Description    : ExceptionHandler
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  public GlobalErrorWebExceptionHandler(ErrorAttributes g, ApplicationContext applicationContext,
      ServerCodecConfigurer serverCodecConfigurer) {

    super(g, new ResourceProperties(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(
      final ErrorAttributes errorAttributes) {

    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  /**
   * 오류 Response 설정
   * 
   * @param request
   * @return
   */
  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

    ResponseDTO errorResponseDTO = new ResponseDTO();

    Throwable error = getError(request);

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    errorResponseDTO.setResultData(true);

    // 에러의 종류가 GatewayExcpetion일 경우 정해진 규격에 맞춰 화면에 전달한다.
    if (error instanceof GatewayException) {

      GatewayException e = (GatewayException) error;

      errorResponseDTO.setResultCode(GatewayExceptionCode.valueOf(e.getMessage()).getCode());

      // 사용자 정의 메시지가 필요할 경우 설정한다.
      if (!StringUtils.isEmptyParams(e.getArg())) {

        errorResponseDTO.setResultMessage(e.getArg());
      } else {

        errorResponseDTO.setResultMessage(GatewayExceptionCode.valueOf(e.getMessage()).getMsg());
      }

      // HttpStatus 설정 변경이 필요하다면 설정
      if (e.getHttpStatus() != null) {

        status = e.getHttpStatus();
      }
    }
    // 그 이외의 정해진 규격이 아닌 Gateway 오류일 경우 아래와 같이 설정한다.
    else {

      errorResponseDTO.setResultCode(GatewayExceptionCode.GWE0001.getCode());
      errorResponseDTO.setResultMessage(error.getMessage());
    }

    return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorResponseDTO));
  }

}
