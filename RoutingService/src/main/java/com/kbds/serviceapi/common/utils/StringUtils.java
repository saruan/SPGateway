package com.kbds.serviceapi.common.utils;

/**
 * 
 *
 * <pre>
 *  Class Name     : StringUtils.java
 *  Description    : String 관련 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-12                 구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class StringUtils {

  /**
   * Null/공백 체크를 하여야 한는 가변 파라미터 체크
   * 
   * @return
   */
  public static boolean isEmptyParams(String... params) {

    for (String param : params) {

      if (param == null || "".equals(param)) {

        return false;
      }
    }
    return true;
  }
}