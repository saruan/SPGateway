package com.kbds.gateway.utils;

import java.util.Map;
import org.springframework.core.io.buffer.DataBuffer;

/**
 *
 * <pre>
 *  Class Name     : WebUtils.java
 *  Description    : Webflux용 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-12    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class WebUtils {

  /**
   * Databuffer -> Map 변환 (전문 추출용)
   * 
   * @param attribute
   * @return
   */
  public static Map<String, String> dataBufferToMap(Object attribute) {

    DataBuffer buffer = (DataBuffer) attribute;
    return StringUtils.queryToMap(buffer);
  }
}
