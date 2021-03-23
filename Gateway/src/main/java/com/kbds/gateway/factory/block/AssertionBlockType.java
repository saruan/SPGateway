package com.kbds.gateway.factory.block;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Data
public class AssertionBlockType implements BlockType {

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void makeFilterData(BlockDTO blockDTO, ServerWebExchange exchange) {

    try {
      Map<String, Object> data;

      /* 현재는 Pre Filter 만 구성 Post Filter 는 성능 이슈 고려 */
      if (GatewayCode.HTTP_REQUEST.getCode().equals(blockDTO.getBlockServlet())) {

        data = FilterUtils.getRequestParams(exchange);
      } else {

        throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST);
      }

      String tid = tracer.currentSpan().context().traceIdString();
      String startTime = DateUtils.getCurrentTime();
      analysisMessage(data, blockDTO);

      /* 큐에 서비스 로그 전송 */
      ServiceLogDTO serviceLog = ServiceLogDTO.builder().tid(tid).message(GatewayCode.SUCCESS
          .getCode()).serviceNm(exchange.getRequest().getURI().getPath()).requestDt(startTime)
          .requestDt(startTime).clientService(GatewayCode.CLIENT_NAME.getCode()).build();

      rabbitTemplate.convertAndSend(GatewayCode.MQ_ROUTING_KEY.getCode(), serviceLog);
    } catch (GatewayException e) {

      throw new GatewayException(GatewayExceptionCode.valueOf(e.getMessage()),
          HttpStatus.BAD_REQUEST, e.getArg());
    } catch (Exception e) {

      throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST, e.toString());
    }
  }

  @Override
  public String getBlockTypeName() {

    return BlockCode.ASSERTION.name();
  }

  /**
   * Logging Message 설정
   *
   * @param data 데이터
   * @return 메시지
   */
  private void analysisMessage(Map<String, Object> data, BlockDTO blockDTO) {

    String compareParameter = blockDTO.getAssertion().getCompareParameter();
    String compareValue = blockDTO.getAssertion().getValue();

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
