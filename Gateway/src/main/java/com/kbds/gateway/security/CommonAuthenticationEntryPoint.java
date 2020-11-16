package com.kbds.gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.kbds.gateway.utils.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * <pre>
 *  Class Name     : CommonAuthenticationEntryPoint.java
 *  Description    : 401(권한없음) 커스텀 오류 관리 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-15    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Component
public class CommonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {

    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

    Map<String, String> data = new HashMap<>();

    data.put("message", e.getMessage());

    byte[] response;
    response = StringUtils.convertToJsonString(data).getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(response);

    return exchange.getResponse().writeWith(Flux.just(buffer));
  }
}
