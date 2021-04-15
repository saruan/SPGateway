package com.kbds.gateway.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <pre>
 *  File  Name     : MockTest
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-31          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MockTest {

  @Mock
  public GatewayFilterChain filterChain;
  @Mock
  public WebClient webClientMock;
  @Mock
  public WebClient.RequestHeadersSpec requestHeadersMock;
  @Mock
  public WebClient.RequestHeadersUriSpec requestHeadersUriMock;
  @Mock
  public WebClient.ResponseSpec responseMock;

}
