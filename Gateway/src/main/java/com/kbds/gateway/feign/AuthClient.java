package com.kbds.gateway.feign;

import com.kbds.gateway.dto.ResponseDTO;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  /**
   * AccessToken 검증 
   * @param token Token 값
   * @param headers 헤더 정보
   * @return 토큰 검증 결과 전문
   */
  @GetMapping(value = "${services.auth.check_token-uri}")
  Map<String, Object> checkAccessToken(@RequestParam(value = "token") String token,
      @RequestHeader Map<String, String> headers);

  /**
   * GatewayCluster 목록 조회
   *
   * @return 클러스터 목록
   */
  @GetMapping(value = "${services.auth.cluster_list}")
  ResponseDTO selectAllClusters();

  /**
   * SAML Assertion 검증
   *
   * @return 검증 결과
   */
  @PostMapping(value = "${services.auth.validate-saml-assertion}")
  ResponseDTO validateSamlAssertion(@RequestParam(value = "saml") String samlAssertion);
}
