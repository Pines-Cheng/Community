package com.forex.common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forex.util.MD5;

public class Test {

	public static void main(String[] args) throws UnknownHostException, IOException, ParseException {
		// TODO Auto-generated method stub
		List<Long> saveIds = new ArrayList<Long>(); 
		Long id = new Long(2);
		saveIds.add(id);
		
		System.out.println(id == 2);
		
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress();
		
		System.out.println(ip);
		
		System.out.println(MD5.encode("xgtz520"));
		
		System.out.println(URLDecoder.decode("你好哈哈shda", "utf8"));
		
		System.out.println("\u6b27\u5143\u7f8e\u5143");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = sdf.parse("2015-04-01 01:00:00");
		Date d2 = sdf.parse("2015-05-01 01:00:00");
		System.out.println(d1.getTime());
		System.out.println(d2.getTime());
		
		String instrument = "ERUUSD";
		System.out.println(instrument.substring(0, 3) + "/" + instrument.substring(3));
		
		System.out.println(1 << 2);
		
		long time = 1436091481;
		Date date = new Date(time * 1000);
		System.out.println(sdf.format(date));
	}
	
	
}
