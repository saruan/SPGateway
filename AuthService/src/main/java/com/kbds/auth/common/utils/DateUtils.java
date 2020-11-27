package com.kbds.auth.common.utils;

import java.util.Date;

/**
 *
 * <pre>
 *  Class Name     : DateUtils.java
 *  Description    : 날짜 관련 공통 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-20    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

  /**
   * 현재시간 YYYY-MM-DD HH:MM:SS로 변환
   * @return  날짜 문자열
   */
  public static Date getExpiredTime(int minutes) {

    return DateUtils.addMinutes(new Date(), minutes);
  }
}
