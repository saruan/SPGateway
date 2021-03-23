package com.kbds.gateway.factory.block;

import brave.Tracer;
import com.kbds.gateway.code.BlockCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDTO;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.DateUtils;
import com.kbds.gateway.utils.FilterUtils;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

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
@Data
public class LogBlockType implements BlockType {

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  @Override
  public void makeFilterData(BlockDTO blockDTO, ServerWebExchange exchange) {

    try {

      String startTime = DateUtils.getCurrentTime();
      String tid = tracer.currentSpan().context().traceIdString();
      String message = blockDTO.getLog().getLogMessage();
      String parameter = blockDTO.getLog().getLogParameter();

      log.info("message : " + message);
      log.info("parameter : " + parameter);

      if (!GatewayCode.BLANK.getCode().equals(parameter)) {

        Map<String, Object> requestParams = FilterUtils.getRequestParams(exchange);

        if (requestParams == null || !requestParams.containsKey(parameter)) {

          message = "Parameter [" + parameter + "] is missing in requestBody";
        } else {

          message =
              "Parameter [" + parameter + "] Value [" + requestParams.get(parameter) +
                  "] Message [" + message + "]";
        }
      }

      log.info(message);

      /* 큐에 서비스 로그 전송 */
      ServiceLogDTO serviceLog = ServiceLogDTO.builder().tid(tid)
          .message(message)
          .serviceNm(exchange.getRequest().getURI().getPath()).requestDt(startTime)
          .requestDt(startTime).clientService(GatewayCode.CLIENT_NAME.getCode()).build();

      rabbitTemplate.convertAndSend(GatewayCode.MQ_ROUTING_KEY.getCode(), serviceLog);
    } catch (Exception e) {

      System.out.println(e.toString());
      throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST, e.toString());
    }
  }

  @Override
  public String getBlockTypeName() {

    return BlockCode.LOG.name();
  }
}
