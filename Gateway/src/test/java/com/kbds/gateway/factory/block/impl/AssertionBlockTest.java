package com.kbds.gateway.factory.block.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.kbds.gateway.code.BlockCode.BlockServlet;
import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.dto.BlockDto.Assertion;
import com.kbds.gateway.dto.ServiceLogDto;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter;
import com.kbds.gateway.filter.system.CachingRequestBodyFilter.Config;
import com.kbds.gateway.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : AssertionBlockTest
 *  Description    : Assertion Test
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-29             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
class AssertionBlockTest {

  CachingRequestBodyFilter cachingRequestBodyFilter;
  MockServerWebExchange exchange;
  GatewayFilter filter;
  ServiceLogDto serviceLog;
  List<Assertion> assertionList;
  BlockDto blockDto;

  @Mock
  GatewayFilterChain filterChain;
  @Mock
  RabbitTemplate rabbitTemplate;
  @Mock
  Tracer tracer;
  @Mock
  Span span;
  @Mock
  TraceContext traceContext;
  @InjectMocks
  AssertionBlock assertionBlock;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    String routeUrl = "http://localhost/get";
    String body = "{\"city\":\"chicago\",\"name\":\"jon doe\",\"age\":\"22\"}";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.post(routeUrl)
        .contentType(MediaType.APPLICATION_JSON).body(body);

    exchange = MockServerWebExchange.from(mockServerHttpRequest);
    ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
    when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());

    serviceLog = ServiceLogDto.builder().tid("test").message(GatewayCode.SUCCESS
        .getCode()).serviceNm("/get").requestDt(DateUtils.getCurrentTime())
        .requestDt(DateUtils.getCurrentTime()).clientService(GatewayCode.CLIENT_NAME.getCode())
        .build();

    /* Body Caching Filter 먼저 수행 */
    cachingRequestBodyFilter = new CachingRequestBodyFilter();
    filter = cachingRequestBodyFilter.apply(new Config());
    filter.filter(exchange, filterChain).block();
  }

  @Test
  void 정상_검증_테스트() {

    Assertion assertion = new Assertion();
    assertion.setValue("chicago");
    assertion.setCompareParameter("city");

    assertionList = new ArrayList<>();
    assertionList.add(assertion);

    blockDto = BlockDto.builder().blockServlet(BlockServlet.REQUEST)
        .blockType(BlockType.ASSERTION).assertion(assertionList).build();

    when(tracer.currentSpan()).thenReturn(span);
    when(span.context()).thenReturn(traceContext);
    when(traceContext.traceIdString()).thenReturn("test");
    doNothing().when(rabbitTemplate)
        .convertAndSend(GatewayCode.SERVICE_LOGGING_ROUTING_KEY.getCode(), serviceLog);

    assertDoesNotThrow(() -> assertionBlock.makeFilterData(blockDto, exchange).block());
  }

  @Test
  void 검증_실패_파라미터_없음() {

    Assertion assertion = new Assertion();
    assertion.setValue("VALUE");
    assertion.setCompareParameter("WRONG_PARAMS");

    assertionList = new ArrayList<>();
    assertionList.add(assertion);

    blockDto = BlockDto.builder().blockServlet(BlockServlet.REQUEST)
        .blockType(BlockType.ASSERTION).assertion(assertionList).build();

    when(tracer.currentSpan()).thenReturn(span);
    when(span.context()).thenReturn(traceContext);
    when(traceContext.traceIdString()).thenReturn("test");

    GatewayException ex = assertThrows(GatewayException.class,
        () -> assertionBlock.makeFilterData(blockDto, exchange).block());

    assertEquals(ex.getMessage(), GatewayExceptionCode.GWE001.name());
  }

  @Test
  void RESPONSE_TYPE_오류(){

    blockDto = BlockDto.builder().blockServlet(BlockServlet.RESPONSE).build();

    GatewayException ex = assertThrows(GatewayException.class,
        () -> assertionBlock.makeFilterData(blockDto, exchange).block());

    assertEquals(ex.getMessage(), GatewayExceptionCode.GWE001.name());
  }
}