package com.kbds.gateway.filter.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.common.MockTest;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.WebClient;
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
public class CommonFilterTest extends MockTest {

  List<String> appKeys;
  RoutingDTO routingDTO;
  GatewayFilter filter;
  CommonFilter commonFilter;

  @Mock
  GatewayFilterChain filterChain;
  @Mock
  private WebClient webClientMock;
  @Mock
  private WebClient.RequestHeadersSpec requestHeadersMock;
  @Mock
  private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
  @Mock
  private WebClient.ResponseSpec responseMock;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    appKeys = new ArrayList<>();
    appKeys.add("testAppKeys");

    routingDTO = RoutingDTO.builder().servicePath("/http://localhost/get")
        .appKeys(appKeys).filterBean("").burstCapacity(20).replenishRate(20)
        .serviceAuthType("OAUTH").serviceLoginType("api_key").serviceNm("서비스 테스트")
        .serviceTargetUrl("https://www.backend.co.kr").build();

    commonFilter = new CommonFilter("http://localhost:7777/oauth/check_token", webClientMock);
    filter = commonFilter.apply(routingDTO);
  }

  @Test
  void API_KEY_정상_필터_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testAppKeys")
        .header(HttpHeaders.AUTHORIZATION, "Bearer dummy_key").build();

    ServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);
    ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
    when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());

    filter.filter(exchange, filterChain).block();

    ServerHttpRequest actualRequest = captor.getValue().getRequest();
    assertEquals(mockServerHttpRequest, actualRequest);
  }

  @Test
  void OAUTH_정상_필터_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testAppKeys")
        .header(HttpHeaders.AUTHORIZATION, "Bearer dummy_key").build();

    ServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    routingDTO.setServiceLoginType("OAUTH");
    filter = commonFilter.apply(routingDTO);

    when(webClientMock.get()).thenReturn(requestHeadersUriMock);
    when(requestHeadersUriMock.uri(anyString(), anyString())).thenReturn(requestHeadersMock);
    when(requestHeadersMock.retrieve()).thenReturn(responseMock);
    when(responseMock.bodyToMono(Map.class)).thenReturn(Mono.empty());

    filter.filter(exchange, filterChain).block();
  }

  @Test
  void API_KEY_오류_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testApKeys").build();

    MockServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    GatewayException ex = assertThrows(GatewayException.class,
        () -> filter.filter(exchange, filterChain));

    assertEquals(ex.getMessage(), GatewayExceptionCode.APP001.getCode());
  }

  @Test
  void OAUTH_TOKEN_없음_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testAppKeys").build();

    routingDTO.setServiceLoginType("OAUTH");
    filter = commonFilter.apply(routingDTO);

    MockServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    GatewayException ex = assertThrows(GatewayException.class,
        () -> filter.filter(exchange, filterChain));

    assertEquals(ex.getMessage(), GatewayExceptionCode.TOK002.getCode());
  }

  @Test
  void OAUTH_TOKEN_오류_테스트() throws IOException {

    String routeUrl = "/";
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.create();
    String baseUrl = String.format("http://localhost:%s",mockWebServer.getPort());
    commonFilter = new CommonFilter(baseUrl + "/oauth/check_token", webClient);

    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testAppKeys")
        .header(HttpHeaders.AUTHORIZATION, "Bearer dummy_key").build();

    ServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    /* 인증서버에서 401 오류 발생 가정 */
    MockResponse mockResponse = new MockResponse()
        .addHeader("api_key", "testAppKeys")
        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer dummy_key")
        .setResponseCode(401).setBody("");

    mockWebServer.enqueue(mockResponse);

    routingDTO.setServiceLoginType("OAUTH");
    filter = commonFilter.apply(routingDTO);

    GatewayException ex = assertThrows(GatewayException.class,
        () -> filter.filter(exchange, filterChain).block());

    assertEquals(ex.getMessage(), GatewayExceptionCode.TOK001.getCode());
    mockWebServer.shutdown();
  }


  @Test
  void 서비스_널_테스트() {

    String routeUrl = "http://localhost/get";
    MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.get(routeUrl)
        .header("api_key", "testAppKeys")
        .header(HttpHeaders.AUTHORIZATION, "Bearer dummy_key").build();

    routingDTO = null;
    filter = commonFilter.apply(routingDTO);

    MockServerWebExchange exchange = MockServerWebExchange.from(mockServerHttpRequest);

    GatewayException ex = assertThrows(GatewayException.class,
        () -> filter.filter(exchange, filterChain));

    assertEquals(ex.getMessage(), GatewayExceptionCode.GWE005.getCode());
  }
}