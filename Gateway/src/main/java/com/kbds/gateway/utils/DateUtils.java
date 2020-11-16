package com.kbds.gateway.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 *
 * <pre>
 *  Class Name     : DateUtils.java
 *  Description    : 날짜 관련 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-16    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class DateUtils {

  /**
   * 현재시간 YYYY-MM-DD HH:MM:SS로 변환
   * @return  날짜 문자열
   */
  public static String getCurrentTime() {

    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }
}
