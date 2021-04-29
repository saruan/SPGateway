package com.kbds.gateway.factory.block.impl;

import brave.Tracer;
import com.kbds.gateway.code.BlockCode.BlockServlet;
import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.dto.BlockDto.Assertion;
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
 *  File  Name     : AssertionBlockType
 *  Description    : Assertion Type
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-23         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@Component
@AllArgsConstructor
public class AssertionBlock implements Block {

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  @Override
  public Mono<Void> makeFilterData(BlockDto blockDTO, ServerWebExchange exchange) {

    return Mono.fromRunnable(() -> {

      try {

        Map<String, Object> data;

        /* 현재는 Pre Filter 만 구성 Post Filter 는 성능 이슈 고려 */
        if (BlockServlet.REQUEST.equals(blockDTO.getBlockServlet())) {

          data = FilterUtils.getRequestParams(exchange);
        } else {

          throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST);
        }

        String tid = tracer.currentSpan().context().traceIdString();
        String startTime = DateUtils.getCurrentTime();

        analysisMessage(data, blockDTO.getAssertion());

        /* 큐에 서비스 로그 전송 */
        ServiceLogDto serviceLog = ServiceLogDto.builder().tid(tid).message(GatewayCode.SUCCESS
            .getCode()).serviceNm(exchange.getRequest().getURI().getPath()).requestDt(startTime)
            .requestDt(startTime).clientService(GatewayCode.CLIENT_NAME.getCode()).build();

        rabbitTemplate
            .convertAndSend(GatewayCode.SERVICE_LOGGING_ROUTING_KEY.getCode(), serviceLog);
      } catch (GatewayException e) {

        throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()),
            HttpStatus.BAD_REQUEST, e.getArg());
      } catch (Exception e) {

        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST,
            e.toString());
      }
    });
  }

  @Override
  public BlockType getBlockTypeName() {

    return BlockType.ASSERTION;
  }

  /**
   * Logging Message 설정
   *
   * @param data 데이터
   * @return 메시지
   */
  public void analysisMessage(Map<String, Object> data, List<Assertion> assertionList) {

    for (Assertion assertion : assertionList) {

      String compareParameter = assertion.getCompareParameter();
      String compareValue = assertion.getValue();

      log.info("compareParameter : " + compareParameter);
      log.info("compareValue : " + compareValue);

      if (!data.containsKey(compareParameter)) {
        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST,
            "Parameter is missing in requestBody");
      } else if (!compareValue.equals(data.get(compareParameter))) {
        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST,
            "Value is not Correct");
      }
    }
  }
}
