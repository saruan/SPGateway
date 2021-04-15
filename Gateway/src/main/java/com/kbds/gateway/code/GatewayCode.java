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

  /* Queue 정보 */
  MQ_ROUTING_KEY("gateway.routing.all", "RabbitMQ 라우팅 키"),

  /* RabbitMQ 정보 */
  CLIENT_NAME("GATEWAY", "RabbitMQ 클라이언트 명칭"),

  /* Filter Servlet 정보 */
  HTTP_REQUEST("REQUEST", "Filter Request 정보"),
  HTTP_RESPONSE("RESPONSE", "Filter Response 정보"),

  DUMMY("", "");

  private final String code;
  private final String desc;
}
