package com.kbds.serviceapi.common.feign;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * <pre>
 *  Class Name     : AuthClient.java
 *  Description    : 인증서버 연동용 FeignClient
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@FeignClient(value = "gateway", url = "${services.bus.host}")
public interface GatewayClient {

  @Async
  @PostMapping(value = "${services.bus.refresh}")
  public Map<String, Object> callRefreshGateway(@RequestHeader Map<String, String> headers);

}
