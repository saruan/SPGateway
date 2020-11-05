package com.kbds.serviceapi.common.feign;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *  File  Name     : AuthClient
 *  Description    : 인증서버 통신 Client
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-04             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@FeignClient(value = "auth", url = "${services.auth.host}")
public interface AuthClient {

  /**
   * Login 후 AccessToken 전달 받는 API 호출

   * @param headers 헤더 정보
   * @param username  사용자 ID
   * @param password  PASSWORD
   * @param scope     Oauth Scope
   * @param grant_type  인증 타입
   * @return
   */
  @PostMapping(value = "${services.auth.token}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  Map<String, Object> login(@RequestHeader Map<String, String> headers,
      @RequestParam String username, @RequestParam String password, @RequestParam String scope,
      @RequestParam String grant_type);
}