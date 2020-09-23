package com.kbds.serviceapi.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
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
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

  /**
   * Null/공백 체크를 하여야 한는 가변 파라미터 체크
   *
   * @return  NULL 체크 여부
   */
  public static boolean isEmptyParams(String... params) {

    for (String param : params) {

      if (param == null || "".equals(param)) {

        return true;
      }
    }
    return false;
  }

  /**
   * App-key 생성
   *
   * @return  생성된 APPKEY
   */
  public static String generateAppKey() {

    return RandomStringUtils.random(25, true, true);
  }
}
