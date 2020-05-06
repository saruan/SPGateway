package com.kbds.gateway.utils;

/**
 * 
 *
 * <pre>
 *  Class Name     : StringUtils.java
 *  Description    : String 관련 커스텀 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-21     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class StringUtils {

  /**
   * 문자열 마지막 제거 메소드
   * 
   * @param str
   * @param regex
   * @param replacement
   * @return
   */
  public static String replaceLast(String str, String regex, String replacement) {

    // 제거하고자 하는 문자의 마지막 index 검색
    int regexIndexOf = str.lastIndexOf(regex);

    if (regexIndexOf == -1) {

      return str;
    } else {

      return str.substring(0, regexIndexOf) + replacement
          + str.substring(regexIndexOf + regex.length());
    }
  }

}
