package com.kbds.gateway.factory.block.impl;

import brave.Tracer;
import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.dto.BlockDto.Log;
import com.kbds.gateway.dto.ServiceLogDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.block.Block;
import com.kbds.gateway.utils.DateUtils;
import com.kbds.gateway.utils.FilterUtils;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : LogBlockType
 *  Description    : Log Type
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-19             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@Component
@AllArgsConstructor
public class LogBlock implements Block {

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  @Override
  public Mono<Void> makeFilterData(BlockDto blockDTO, ServerWebExchange exchange) {

    return Mono.fromRunnable(() -> {

      try {

        List<Log> logList = blockDTO.getLog();
        Map<String, Object> requestParams = FilterUtils.getRequestParams(exchange);

        /* 로그 메시지 적재 */
        setLogMessage(requestParams, logList, exchange);

      } catch (Exception e) {

        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST,
            e.toString());
      }
    });
  }

  @Override
  public BlockType getBlockTypeName() {

    return BlockType.LOG;
  }

  /**
   * 로그 메시지 적재
   *
   * @param requestParams 파라미터
   * @param logList       로깅할 정보
   * @param exchange      ServerWebExchange
   */
  public void setLogMessage(Map<String, Object> requestParams, List<Log> logList,
      ServerWebExchange exchange) {

    for (Log logBlock : logList) {

      String startTime = DateUtils.getCurrentTime();
      String tid = tracer.currentSpan().context().traceIdString();
      String message = logBlock.getLogMessage();
      String parameter = logBlock.getLogParameter();

      if (!GatewayCode.BLANK.getCode().equals(parameter)) {

        if (requestParams == null || !requestParams.containsKey(parameter)) {

          message = "Parameter [" + parameter + "] is missing in requestBody";
        } else {

          message =
              "Parameter [" + parameter + "] Value [" + requestParams.get(parameter) +
                  "] Message [" + message + "]";
        }
      }

      log.info("parameter : " + parameter);
      log.info(message);

      /* 큐에 서비스 로그 전송 */
      ServiceLogDto serviceLog = ServiceLogDto.builder().tid(tid)
          .message(message).serviceNm(exchange.getRequest().getURI().getPath())
          .requestDt(startTime)
          .requestDt(startTime).clientService(GatewayCode.CLIENT_NAME.getCode()).build();

      rabbitTemplate
          .convertAndSend(GatewayCode.SERVICE_LOGGING_ROUTING_KEY.getCode(), serviceLog);
    }
  }
}
