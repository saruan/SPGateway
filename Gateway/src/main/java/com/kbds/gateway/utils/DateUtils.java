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

  public static String getCurrentTime() {

    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }
}
