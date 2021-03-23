package com.kbds.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;

/**
 * <pre>
 *  File  Name     : FilterUtils
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-23          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class FilterUtils {

  final static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 서블릿 종류가 요청일 경우 데이터 적재
   *
   * @param exchange ServerWebExchange
   * @return 요청 데이터
   */
  public static Map<String, Object> getRequestParams(ServerWebExchange exchange)
      throws JsonProcessingException {

    /* Request Body 추출 */
    Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
    String requestBody = "";

    DataBuffer buffer = (DataBuffer) attribute;

    if (buffer != null) {

      requestBody = StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString();
    }

    if (!isJSONValid(requestBody)) {

      return new HashMap<>();
    } else {

      return objectMapper.readValue(requestBody, Map.class);
    }
  }

  /**
   * JSON String 여부 체크
   *
   * @param params 전문
   * @return JSON 양식 결과 유무
   */
  public static boolean isJSONValid(String params) {

    try {

      if(GatewayCode.BLANK.getCode().equals(params)){

        return false;
      }

      objectMapper.readTree(params);

      return true;
    } catch (IOException e) {

      return false;
    }
  }
}
