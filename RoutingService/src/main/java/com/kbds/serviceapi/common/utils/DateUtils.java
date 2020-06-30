package com.kbds.serviceapi.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *  Class Name     : DateUtils.java
 *  Description    : Date 관련 Util 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {


  /**
   * yyyy-mm-dd hh:mm 타입의 날짜를 리턴
   *
   * @return
   */
  public static String getCurrentDate() {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date time = new Date();

    return format.format(time);
  }

}
