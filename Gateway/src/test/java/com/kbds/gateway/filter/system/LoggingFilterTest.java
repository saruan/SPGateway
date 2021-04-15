package com.kbds.gateway.filter.system;

import com.kbds.gateway.GatewayApplication;
import com.kbds.gateway.common.MockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

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

public class LoggingFilterTest extends MockTest {

  WebTestClient webTestClient;

  @Autowired
  private ApplicationContext context;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    this.webTestClient = WebTestClient.bindToApplicationContext(this.context).build();
  }

  @Test
  void 정상_로그_테스트() {
    webTestClient.post().uri("/post").header("Host", "www.modifyresponsebodyjavatoolarge.org")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(BodyInserters.fromValue("test")).exchange().expectStatus().isOk();
  }

  @Configuration(proxyBeanMethods = false)
  @Import(GatewayApplication.class)
  public static class TestConfig {

    @Bean
    public RouteLocator testRouteLocator(RouteLocatorBuilder builder) {

      return builder.routes().route(p -> p.path("/post/**").uri("http://httpbin.org:80")).build();
    }
  }
}