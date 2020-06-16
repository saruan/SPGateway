package com.kbds.gateway.filter;

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
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.utils.DateUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 *
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
 *
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

  @Autowired
  private KafkaTemplate<String, ServiceLogDTO> kafkaTemplate;

  // kafka 토픽
  private final String GATEWAY_TOPIC = "GATEWAY_LOG";

  private final String SERVICE_NAME = "GATEWAY";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String startTime = DateUtils.getCurrentTime();

    // Body 추출 이후 임시 저장
    Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
    StringBuilder body = new StringBuilder();

    if (attribute instanceof DataBuffer) {

      DataBuffer buffer = (DataBuffer) attribute;
      body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
    }

    // POST 필터 호출 시 Kafka를 통해 서비스 로그 전송
    return chain.filter(exchange).then(Mono.fromRunnable(() -> {

      String endTime = DateUtils.getCurrentTime();

      // 큐에 서비스 로그 전송
      ServiceLogDTO serviceLog =
          new ServiceLogDTO(exchange.getRequest().getHeaders().toString(), body.toString(),
              exchange.getResponse().getStatusCode().toString(), SERVICE_NAME, startTime, endTime);
      kafkaTemplate.send(GATEWAY_TOPIC, serviceLog);
    }));
  }

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE;
  }

  /**
   * Response 시 서비스 로깅을 위해 Body값을 읽어오는 <메소드 현재 사용 안함>
   * 
   * @param exchange
   * @return
   */
  ServerHttpResponseDecorator logResponse(ServerWebExchange exchange, String queryParam) {

    ServerHttpResponse origResponse = exchange.getResponse();

    DataBufferFactory bufferFactory = origResponse.bufferFactory();

    return new ServerHttpResponseDecorator(origResponse) {
      @Override
      public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        // DataBuffer 변환
        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

        return super.writeWith(fluxBody.map(dataBuffer -> {

          // Body Content를 읽어온다.
          byte[] content = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(content);
          // String bodyContent = new String(content, StandardCharsets.UTF_8);

          // 큐에 서비스 로그 전송
          // ServiceLogDTO serviceLog =
          // new ServiceLogDTO(exchange.getRequest().getHeaders().toString(),
          // queryParam.toString(), bodyContent, SERVICE_NAME);
          // kafkaTemplate.send(GATEWAY_TOPIC, serviceLog);

          return bufferFactory.wrap(content);
        }));
      }
    };
  }
}
