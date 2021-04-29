package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  Class Name     : GatewayCode.java
 *  Description    : Gateway 공통 상수 코드
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

  /* CommonCode */
  Y("Y", "예"),
  N("N", "아니오"),
  EMPTY("", "공백"),
  ROLE_ADMIN("ROLE_ADMIN", "관리자 권한"),
  GATEWAY_FILTER_APPLY("apply", "Gateway Filter 메소드명"),
  SERVICE_NAME("GATEWAY", "현재 서비스 이름"),
  BLANK("", "공백"),
  SUCCESS("success", "오류 없이 성공"),
  SUCCESS_CODE("200", "공통 성공 코드"),

  /* Params, Header 정보 */
  CACHE_REQUEST_BODY("cachedRequestBody", "Request Body Caching 데이터"),
  TOKEN_PREFIX("Bearer ", "TOKEN 헤더 PREFIX"),
  HS_KEY("hsKey","전문 위변조 검증 파라미터"),

  /* Queue 정보 */
  SYSTEM_ROUTING_KEY("gateway.routing.system", "Global Logging Rabbit Routing Key"),
  ERROR_ROUTING_KEY("gateway.routing.error", "Global Error Logging Rabbit Routing Key"),
  SERVICE_LOGGING_ROUTING_KEY("gateway.routing.services", "Custom Logging Rabbit Routing Key"),

  /* RabbitMQ 정보 */
  CLIENT_NAME("GATEWAY", "RabbitMQ 클라이언트 명칭"),

  DUMMY("", "");

  private final String code;
  private final String desc;
}
