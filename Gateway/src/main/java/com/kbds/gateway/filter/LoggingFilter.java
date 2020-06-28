package com.kbds.gateway.filter;

import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.utils.DateUtils;
import java.nio.charset.StandardCharsets;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  Class Name     : LogginFilter.java
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
public class LoggingFilter implements GlobalFilter, Ordered {

  @Autowired
  private KafkaTemplate<String, ServiceLogDTO> kafkaTemplate;

  private final String GATEWAY_TOPIC = "GATEWAY_LOG";
  private final String CLIENT_NAME = "GATEWAY";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String startTime = DateUtils.getCurrentTime();

    // POST 필터 호출 시 Kafka를 통해 서비스 로그 전송
    return chain.filter(exchange.mutate().response(logResponse(exchange, startTime)).build());
  }

  @Override
  public int getOrder() {

    return Integer.MIN_VALUE;
  }

  /**
   * Service Logging 수행
   *
   * @param exchange
   * @param startTime
   * @return
   */
  ServerHttpResponseDecorator logResponse(ServerWebExchange exchange, String startTime) {

    ServerHttpResponse origResponse = exchange.getResponse();
    DataBufferFactory bufferFactory = origResponse.bufferFactory();

    return new ServerHttpResponseDecorator(origResponse) {
      @Override
      public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        String endTime = DateUtils.getCurrentTime();
        String headerInfo = exchange.getRequest().getHeaders().toSingleValueMap().toString();
        String appKey = exchange.getRequest().getHeaders()
            .getFirst(GatewayCode.API_KEY.getCode());

        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
        return super.writeWith(fluxBody.map(dataBuffer -> {

          // Body Content를 읽어온다.
          byte[] content = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(content);
          String responseBody = new String(content, StandardCharsets.UTF_8);

          // Body 추출 이후 임시 저장
          Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
          StringBuilder requestBody = new StringBuilder();

          if (attribute instanceof DataBuffer) {

            DataBuffer buffer = (DataBuffer) attribute;
            requestBody.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
          }

          // 큐에 서비스 로그 전송
          ServiceLogDTO serviceLog =
              new ServiceLogDTO(headerInfo, requestBody.toString(), responseBody, appKey,
                  exchange.getRequest().getURI().getPath(), CLIENT_NAME, startTime,
                  endTime);
          kafkaTemplate.send(GATEWAY_TOPIC, serviceLog);

          return bufferFactory.wrap(content);
        }));
      }
    };
  }
}
