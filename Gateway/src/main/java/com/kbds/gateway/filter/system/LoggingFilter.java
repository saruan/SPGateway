package com.kbds.gateway.filter.system;

import brave.Tracer;
import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.ServiceLogDTO;
import com.kbds.gateway.utils.DateUtils;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  Class Name     : LoggingFilter.java
 *  Description    : Logging 용 Global Filter
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-15    	       구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@AllArgsConstructor
@Service
public class LoggingFilter implements GlobalFilter, Ordered {

  private final RabbitTemplate rabbitTemplate;
  private final Tracer tracer;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String startTime = DateUtils.getCurrentTime();

    return chain.filter(exchange.mutate().response(logResponse(exchange, startTime)).build());
  }

  @Override
  public int getOrder() {

    return Integer.MIN_VALUE;
  }

  /**
   * Service Logging 수행
   *
   * @param exchange  ServerWebExchange 객체
   * @param startTime Request 시작 시간
   * @return ServerHttpResponseDecorator 객체
   */
  ServerHttpResponseDecorator logResponse(ServerWebExchange exchange, String startTime) {

    ServerHttpResponse origResponse = exchange.getResponse();
    DataBufferFactory bufferFactory = origResponse.bufferFactory();

    return new ServerHttpResponseDecorator(origResponse) {
      @Override
      public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        String endTime = DateUtils.getCurrentTime();
        String headerInfo = exchange.getRequest().getHeaders().toSingleValueMap().toString();
        String appKey = exchange.getRequest().getHeaders().getFirst(AuthTypeCode.API_KEY.getCode());
        String servicePath = exchange.getRequest().getURI().getPath();
        String tid = tracer.currentSpan().context().traceIdString();

        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

        return super.writeWith(fluxBody.map(dataBuffer -> {

          /* Response Body 추출 */
          byte[] content = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(content);
          String responseBody = new String(content, StandardCharsets.UTF_8);

          /* Request Body 추출 */
          Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
          String requestBody = "";

          if (attribute instanceof DataBuffer) {

            DataBuffer buffer = (DataBuffer) attribute;
            requestBody = StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString();
          }

          /* 큐에 서비스 로그 전송 */
          ServiceLogDTO serviceLog = ServiceLogDTO.builder().tid(tid).requestHeader(headerInfo)
              .requestParams(requestBody).response(responseBody).appKey(appKey)
              .serviceNm(servicePath).clientService(GatewayCode.CLIENT_NAME.getCode())
              .requestDt(startTime).responseDt(endTime).build();

          rabbitTemplate.convertAndSend(GatewayCode.MQ_ROUTING_KEY.getCode(), serviceLog);

          return bufferFactory.wrap(content);
        }));
      }
    };
  }
}
