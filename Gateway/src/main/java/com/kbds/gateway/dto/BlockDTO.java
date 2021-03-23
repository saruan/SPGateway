package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : BlockDTO
 *  Description    : API 로 조립한 Filter 명세 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-19          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDTO {

  /* Block Type */
  private String blockType;
  /* Servlet Type */
  private String blockServlet;
  /* Log Type */
  private Log log;
  /* Assertion Type */
  private Assertion assertion;

  /**
   * Log Class
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Log extends BlockDTO {

    /* 로깅할 파라미터명  */
    private String logParameter;
    /* 로깅할 메시지 */
    private String logMessage;
  }

  /**
   * Assertion Class
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Assertion extends BlockDTO {

    /* 비교 파라미터 명 */
    private String compareParameter;
    /* 파라미터 값 */
    private String value;
  }
}
