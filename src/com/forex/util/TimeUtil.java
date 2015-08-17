package com.forex.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static long getCurrentTime() {
		return new Date().getTime() / 1000;
	}
	
	public static String format(long timeMills) {
		Date date = new Date(timeMills);
		SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
		String timeString = format.format(date);
		return timeString;
	}
}
