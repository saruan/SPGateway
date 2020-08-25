package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  Class Name     : GatewayCode.java
 *  Description    : Gateway 공통 코드
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-04     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum GatewayCode {

  Y("Y", "예"),
  N("N", "아니오"),
  EMPTY("", "공백"),
  ROLE_ADMIN("ROLE_ADMIN", "관리자 권한"),
  GATEWAY_FILTER_APPLY("apply", "Gateway Filter 메소드명"),
  API_KEY("api_key", "appKey 헤더 정보"),
  CACHE_REQUEST_BODY("cachedRequestBody", "Request Body Caching 데이터"),

  TOKEN_PREFIX("Bearer ", "TOKEN 헤더 PREFIX"),
  APP_KEY_PREFIX(",", "APP-KEY 구분자"),

  OAUTH_TYPE("1", "OAuth 인증 방식"),

  MQ_ROUTING_KEY("gateway.routing.all", "RabbitMQ 라우팅 키"),

  DUMMY("", "");

  private String code;
  private String desc;
}
