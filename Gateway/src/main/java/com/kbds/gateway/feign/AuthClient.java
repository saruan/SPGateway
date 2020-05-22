package com.kbds.gateway.feign;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
@FeignClient(value = "auth", url = "${services.auth.host}")
public interface AuthClient {

  @GetMapping(value = "${services.auth.check_token-uri}")
  public Map<String, Object> checkAccessToken(@RequestParam(value = "token") String token,
      @RequestHeader Map<String, String> headers);

}
