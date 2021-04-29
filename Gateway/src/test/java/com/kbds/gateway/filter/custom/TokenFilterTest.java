package com.kbds.gateway.filter.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.common.MockTest;
import com.kbds.gateway.dto.RoutingDto;
import com.kbds.gateway.dto.TokenDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.grant.Grant;
import com.kbds.gateway.factory.grant.GrantTypeFactory;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter.Config;
import com.kbds.gateway.filter.system.TokenFilter;
import com.kbds.gateway.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     :
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0                	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
class TokenFilterTest{

  TokenFilter tokenFilter;
  CachingRequestBodyFilter cachingRequestBodyFilter;
  GatewayFilter filter;

  @Mock
  public GatewayFilterChain filterChain;

  @Mock
  Grant grant;

  @Mock
  GrantTypeFactory grantTypeFactory;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    tokenFilter = new TokenFilter("Basic R2F0ZXdheVBvcnRhbDpHYXRld2F5UG9ydGFsIQ==",
        "test", "test", "scope", grantTypeFactory);
    filter = tokenFilter.apply(new RoutingDto());
  }

  @Test
  void 정상_필터_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.post(routeUrl)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(
            "username=user&password=pass&grant_type=password&scope=read_profile&jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDT01NT05fQ0xVU1RFUiJ9.rWMSY921vwExb_jipZEzAE5WVWy7EIef-PJFd-q6nWU");

    MockServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);
    ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
    when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());

    /* Body Caching Filter 먼저 수행 */
    cachingRequestBodyFilter = new CachingRequestBodyFilter();
    filter = cachingRequestBodyFilter.apply(new Config());
    filter.filter(exchange, filterChain).block();

    Map<String, String> queryParam = StringUtils.queryToMap(
        Objects.requireNonNull(exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode())));
    when(grantTypeFactory.makeGrantType("password")).thenReturn(grant);
    doNothing().when(grant).validateParameters(queryParam);

    /* 실제 테스트 필터 수행 */
    filter = tokenFilter.apply(new RoutingDto());
    filter.filter(exchange, filterChain).block();

    assertEquals("Basic R2F0ZXdheVBvcnRhbDpHYXRld2F5UG9ydGFsIQ==",
        exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
  }

  @Test
  void 파라미터_없음_오류(){

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.post(routeUrl)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).build();

    ServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    /* 실제 테스트 필터 수행 */
    filter = tokenFilter.apply(new RoutingDto());

    GatewayException ex = assertThrows(GatewayException.class,
        () -> filter.filter(exchange, filterChain));

    assertEquals(ex.getMessage(), GatewayExceptionCode.GWE002.name());
  }
}