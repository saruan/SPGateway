package com.kbds.gateway.config;

import com.kbds.gateway.GatewayApplication;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.RoutingCode.ServiceAuthType;
import com.kbds.gateway.code.RoutingCode.ServiceLoginType;
import com.kbds.gateway.common.MockTest;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.dto.RoutingDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

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
class RoutingConfigurationTest extends MockTest {

  @Autowired
  private ApplicationContext context;

  List<String> appKeys;
  List<RoutingDto> routingDtoList;
  RoutingDto routingDTO;
  ResponseDto responseDTO;
  WebTestClient webTestClient;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    appKeys = new ArrayList<>();
    appKeys.add("testAppKeys");

    routingDTO = RoutingDto.builder().servicePath("/http://localhost/get")
        .appKeys(appKeys).filterBean("").burstCapacity(20).replenishRate(20)
        .serviceAuthType(ServiceAuthType.PUBLIC).serviceLoginType(ServiceLoginType.API_KEY)
        .serviceNm("서비스 테스트").serviceTargetUrl("https://www.backend.co.kr").build();

    routingDtoList = new ArrayList<>();
    routingDtoList.add(routingDTO);

    responseDTO = new ResponseDto();
    responseDTO.setResultCode("200");
    responseDTO.setResultMessage(GatewayCode.SUCCESS.getCode());
    responseDTO.setResultData(routingDtoList);

    this.webTestClient = WebTestClient.bindToApplicationContext(this.context).build();
  }

  @Test
  void API_정상_등록_테스트() {

    webTestClient.get().uri("/post").exchange().expectStatus().isOk();
  }


  @Configuration(proxyBeanMethods = false)
  @Import(GatewayApplication.class)
  public static class TestConfig {

    @Autowired
    RoutingConfiguration routingConfiguration;

    @Bean
    public RouteLocator testRouteLocator(RouteLocatorBuilder builder) throws Exception {

      List<String> appKeys = new ArrayList<>();
      appKeys.add("testAppKeys");

      RoutingDto routingDTO = RoutingDto.builder().servicePath("/post")
          .appKeys(appKeys).burstCapacity(20).replenishRate(20)
          .serviceAuthType(ServiceAuthType.PUBLIC).serviceLoginType(ServiceLoginType.API_KEY)
          .serviceNm("서비스 테스트").serviceTargetUrl("https://httpbin.org/get").build();

      RouteLocatorBuilder.Builder routeLocator = builder.routes();

      routingConfiguration.registerRouterLocator(routeLocator, routingDTO);

      return routeLocator.build();
    }
  }
}