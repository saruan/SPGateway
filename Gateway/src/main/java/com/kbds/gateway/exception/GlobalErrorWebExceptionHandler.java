package com.kbds.gateway.exception;

import brave.Tracer;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.dto.ServiceLogDto;
import com.kbds.gateway.utils.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  public GlobalErrorWebExceptionHandler(ErrorAttributes g, ApplicationContext applicationContext,
      ServerCodecConfigurer serverCodecConfigurer, RabbitTemplate rabbitTemplate, Tracer tracer) {

    super(g, new ResourceProperties(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
    this.rabbitTemplate = rabbitTemplate;
    this.tracer = tracer;
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
   */
  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

    ResponseDto errorResponseDto = new ResponseDto();
    errorResponseDto.setResultData(true);

    String tid = tracer.currentSpan().context().traceIdString();
    String header = request.headers().asHttpHeaders().toString();
    Throwable error = getError(request);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    /* 에러의 종류가 GatewayException 경우 정해진 규격에 맞춰 화면에 전달한다. */
    if (error instanceof GatewayException) {

      GatewayException e = (GatewayException) error;

      errorResponseDto.setResultCode(GatewayExceptionCode.valueOf(e.getMessage()).name());
      errorResponseDto.setResultMessage(GatewayExceptionCode.valueOf(e.getMessage()).getMsg());

      /* HttpStatus 설정 변경이 필요하다면 설정 */
      if (e.getHttpStatus() != null) {

        status = e.getHttpStatus();
      }

      if(e.getArg() != null){

        errorResponseDto.setResultMessage(e.getArg());
      }

      String currentTime = DateUtils.getCurrentTime();

      /* 큐에 오류 로그 전송 */
      ServiceLogDto serviceLog = ServiceLogDto.builder().tid(tid).requestHeader(header)
          .requestParams(e.getArg()).response(errorResponseDto.toString())
          .clientService(GatewayCode.CLIENT_NAME.getCode()).requestDt(currentTime)
          .responseDt(currentTime).build();

      rabbitTemplate.convertAndSend(GatewayCode.ERROR_ROUTING_KEY.getCode(), serviceLog);
    }
    /* 그 이외의 정해진 규격이 아닌 Gateway 오류일 경우 아래와 같이 설정한다. */
    else {

      errorResponseDto.setResultCode(GatewayExceptionCode.GWE001.name());
      errorResponseDto.setResultMessage(error.getMessage());
    }

    return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorResponseDto));
  }
}
