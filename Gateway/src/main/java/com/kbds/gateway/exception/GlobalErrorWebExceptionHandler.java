package com.kbds.gateway.exception;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.utils.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
import reactor.core.publisher.Mono;

/**
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
 */
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  @Autowired
  private RabbitTemplate rabbitTemplate;

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
   * @param request ServerRequest 객체
   * @request response
   */
  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

    ResponseDTO errorResponseDTO = new ResponseDTO();
    errorResponseDTO.setResultData(true);

    Throwable error = getError(request);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    // 에러의 종류가 GatewayExcpetion일 경우 정해진 규격에 맞춰 화면에 전달한다.
    if (error instanceof GatewayException) {

      GatewayException e = (GatewayException) error;

      errorResponseDTO.setResultCode(GatewayExceptionCode.valueOf(e.getMessage()).getCode());
      errorResponseDTO.setResultMessage(GatewayExceptionCode.valueOf(e.getMessage()).getMsg());

      // HttpStatus 설정 변경이 필요하다면 설정
      if (e.getHttpStatus() != null) {

        status = e.getHttpStatus();
      }

      String currentTime = DateUtils.getCurrentTime();

      // 큐에 오류 로그 전송
      ServiceLogDTO serviceLog = new ServiceLogDTO(request.headers().asHttpHeaders().toString(),
          e.getArg(), errorResponseDTO.toString(), GatewayCode.BLANK.getCode(),
          GatewayCode.BLANK.getCode(), GatewayCode.SERVICE_NAME.getCode(), currentTime, currentTime);
      rabbitTemplate.convertAndSend(GatewayCode.MQ_ROUTING_KEY.getCode(), serviceLog);
    }
    // 그 이외의 정해진 규격이 아닌 Gateway 오류일 경우 아래와 같이 설정한다.
    else {

      errorResponseDTO.setResultCode(GatewayExceptionCode.GWE001.getCode());
      errorResponseDTO.setResultMessage(error.getMessage());
    }

    return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorResponseDTO));
  }
}
