package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbds.gateway.code.BlockCode.BlockServlet;
import com.kbds.gateway.code.BlockCode.BlockType;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BlockDto {

  /* Block Type */
  private BlockType blockType;
  /* Servlet Type */
  private BlockServlet blockServlet;
  /* Log Type */
  @Builder.Default
  private List<Log> log = new ArrayList<>();
  /* Assertion Type */
  @Builder.Default
  private List<Assertion> assertion = new ArrayList<>();
  /* Reactive 형태의 Rest Type */
  private ReactiveRest reactiveRest;

  /**
   * Log Class
   */
  @Data
  @EqualsAndHashCode(callSuper = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Log extends BlockDto {

    /* 로깅할 파라미터명  */
    private String logParameter;
    /* 로깅할 메시지 */
    private String logMessage;
  }

  /**
   * Assertion Class
   */
  @Data
  @EqualsAndHashCode(callSuper = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Assertion extends BlockDto {

    /* 비교 파라미터 명 */
    private String compareParameter;
    /* 파라미터 값 */
    private String value;
  }

  /**
   * Reactive Rest Class
   */
  @Data
  @NoArgsConstructor
  @SuperBuilder
  @EqualsAndHashCode(callSuper = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ReactiveRest extends BlockDto{

    /* 호출 URL */
    private String url;
    /* HTTP METHOD */
    private String method;
    /* Contents-Type */
    private String contentsType;
    /* 호출 결과 기대 Http Status */
    private int expectStatus;
    /* 호출 결과 Assertions */
    @Builder.Default
    private List<Assertion> restAssertions = new ArrayList<>();
    /* 호출 결과 Logs */
    @Builder.Default
    private List<Log> restLogs = new ArrayList<>();
  }
}
