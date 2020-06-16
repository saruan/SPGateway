package com.kbds.gatewaylog.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.text.ParseException

class DateUtils {
	
	companion object {
		
		@Throws(ParseException::class)
		fun validateDateFormat(date: String) {
		
    		var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    		formatter.parse(date)
    	}
	}
}