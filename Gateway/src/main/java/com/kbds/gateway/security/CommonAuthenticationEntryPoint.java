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

@Component
public class CommonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {

    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

    Map<String, String> data = new HashMap<String, String>();

    data.put("message", e.getMessage());

    byte[] response;
    response = StringUtils.convertToJsonString(data).getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(response);

    return exchange.getResponse().writeWith(Flux.just(buffer));
  }



}
