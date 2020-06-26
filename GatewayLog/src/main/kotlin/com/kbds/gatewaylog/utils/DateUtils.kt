package com.kbds.gatewaylog.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 *  Class Name     : DateUtils.kt
 *  Description    : 날짜 Utility 기능
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     		   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
class DateUtils {

    companion object {

        /**
         * 날짜 타입 유효성 체크 메소드
         */
        @Throws(ParseException::class)
        fun validateDateFormat(vararg dates: String) {

            for (date in dates) {

                var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.parse(date)
            }
        }
    }
}