package com.forex.model.bean;

import java.util.HashMap;
import java.util.Map;

public class Currency {
	
	public static final String[] Code = { "EUR", "USD", "XAU", "GBP", "JPY",
			"AUD", "CHF", "CAD", "NZD", "CNY" };

	public static final String[] Country = { "欧元", "美元", "黄金", "英镑", "日元",
			"澳元", "瑞郎", "加元", "纽元", "人民币" };
	
	public static final String[] CodePair = { "EUR/USD", "XAU/USD", "GBP/USD",
			"EUR/JPY", "USD/JPY", "GBP/JPY", "AUD/JPY", "USD/CHF", "GBP/CHF",
			"AUD/USD", "EUR/AUD", "EUR/CHF", "EUR/CAD", "EUR/GBP", "NZD/USD",
			"USD/CAD", "NZD/JPY", "AUD/NZD", "EUR/NZD" };
	
	/**
	 * 货币对
	 */
	public static Map<String, String> Combination = new HashMap<String, String>();
	
	/**
	 * 货币代号
	 */
	public static Map<String, String> CountryCode = new HashMap<String, String>(); 
	
	public static Map<String, String> CountryImage = new HashMap<String, String>();
	
	static {
		for (int i = 0; i < Code.length; i++) {
			CountryCode.put(Code[i], Country[i]);
		}
		for (int i = 0; i < CodePair.length; i++) {
			String[] strArr = CodePair[i].split("/");
			Combination.put(CodePair[i], String.format("%s/%s",
					getChineseNameByCode(strArr[0]),
					getChineseNameByCode(strArr[1])));
		}
		CountryImage.put("美国", "america");
		CountryImage.put("加拿大", "canada");
		CountryImage.put("中国", "china");
		CountryImage.put("英国", "england");
		CountryImage.put("欧元区", "european_union");
		CountryImage.put("日本", "japan");
		CountryImage.put("瑞士", "switzerland");
		CountryImage.put("法国", "franch");
		CountryImage.put("意大利", "italy");
		CountryImage.put("澳大利亚", "australia");
		CountryImage.put("新西兰", "zealand");
		CountryImage.put("德国", "germany");
		CountryImage.put("希腊", "greece");
		CountryImage.put("香港", "hongkong");
		CountryImage.put("韩国", "korea");
		CountryImage.put("新加坡", "singapore");
		CountryImage.put("西班牙", "spain");
	}
	
	public static String getChineseNameByCode(String code) {
		return CountryCode.get(code);
	}
	
	public static String getChineseNameByPair(String pair) {
		return Combination.get(pair);
	}
	
}
