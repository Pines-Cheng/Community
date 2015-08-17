package com.forex.util;

import java.util.Random;

public class KeyUtil {

	private static final String ELEMENT = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	
	public static String createKey(int len) {
		String key = "";
		Random rand = new Random();
		for (int i=0; i<len; i++) {
			int pos = rand.nextInt(ELEMENT.length());
			key += ELEMENT.charAt(pos);
		}
		return key;
	}
	
	/**
	 * 生成短信验证码
	 * @return 6位的短信验证码
	 */
	public static String createCode() {
		String key = "";
		Random rand = new Random();
		for (int i=0; i<6; i++) {
			int pos = rand.nextInt(10);
			key += ELEMENT.charAt(pos);
		}
		return key;
	}
}
