package com.kbds.gateway.filter;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.utils.DateUtils;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  Class Name     : GlobalLoggingFilter.java
 *  Description    : Logging용 Global Filter
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-15    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Component
public class GlobalLoggingFilter extends AbstractGatewayFilterFactory<RoutingDTO> {

  private final String GATEWAY_TOPIC = "GATEWAY_LOG";
  private final String CLIENT_NAME = "GATEWAY";
  private final Logger logger = LoggerFactory.getLogger(GlobalLoggingFilter.class);
  private KafkaTemplate<String, ServiceLogDTO> kafkaTemplate;

  @Autowired
  public GlobalLoggingFilter(KafkaTemplate<String, ServiceLogDTO> kafkaTemplate) {

    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      String startTime = DateUtils.getCurrentTime();

      String appKey = exchange.getRequest().getHeaders().getFirst(GatewayCode.API_KEY.getCode());

      // Body 추출 이후 임시 저장
      Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
      StringBuilder body = new StringBuilder();

      if (attribute instanceof DataBuffer) {

        DataBuffer buffer = (DataBuffer) attribute;
        body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
      }

      return chain.filter(exchange).then(Mono.fromRunnable(() -> {

        String endTime = DateUtils.getCurrentTime();
        String headerInfo = exchange.getRequest().getHeaders().toSingleValueMap().toString();
        String httpStatusCode = exchange.getResponse().getStatusCode().name();

        // 큐에 서비스 로그 전송
        ServiceLogDTO serviceLog =
            new ServiceLogDTO(headerInfo, body.toString(), httpStatusCode, appKey,
                routingDTO.getServiceNm(), CLIENT_NAME, startTime,
                endTime);
        kafkaTemplate.send(GATEWAY_TOPIC, serviceLog);
      }));
    };
  }
}
