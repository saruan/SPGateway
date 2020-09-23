package com.kbds.auth.common.utils;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  /**
   * Null/공백 체크를 하여야 한는 가변 파라미터 체크
   * 
   * @return
   */
  public static boolean isEmptyParams(String... params) {

    for (String param : params) {

      if (param == null || "".equals(param)) {

        return true;
      }
    }

    return false;
  }


  public static String convertToJsonString(Map<String, String> param) {


    ObjectMapper mapper = new ObjectMapper();

    try {

      return mapper.writeValueAsString(param);
    } catch (JsonProcessingException e) {

      return "";
    }
  }

}
