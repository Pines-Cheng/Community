package com.forex.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSONParser {
	Map<String, String> map = new HashMap<String, String>();

	public String parse(String json, String name) throws IOException {
		String str = clip(json);
		for (String pair : str.split(",")) {
			String arr[] = pair.split(":");
			
			if (arr.length < 2) {
				System.out.println(arr[0]);
				continue;
			}
			
			if (arr[1].matches("^[0-9]*$")) {
				map.put(clip(arr[0]), "int");
			} else if (arr[1].matches("[0-9]*.[0-9]*")) {
				map.put(clip(arr[0]), "float");
			} else if (arr[1].equals("true") || arr[1].equals("false")) {
				map.put(clip(arr[0]), "boolean");
			} else {
				map.put(clip(arr[0]), "String");
			}
		}
		return write(name);
	}

	private String write(String name) {
		String result = "";
		String classname = toUpperCase(name);
		result += "public class " + classname + "{\r\n\r\n";
		
		for (Entry<String, String> entry : map.entrySet()) {
			result += "	private " + entry.getValue() + " " + convert(entry.getKey()) + ";\r\n";
		}
		result += "\r\n";
		
		result += "	public static " + classname + " parse" + classname + " (JSONObject json) {\r\n";
		result += "		" + classname + " " + name + " = new " + classname + "();\r\n";
		
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals("float")) {
				result +=  "		" + name + "." + convert(entry.getKey()) + " = (float) json.optDouble(\"" + entry.getKey() + "\", 0);\r\n";
			} else if (entry.getValue().equals("String")) {
				result += "		if (!json.isNull(" + "\"" + entry.getKey() + "\"" + ")) {\r\n";
				result +=  "			" + name + "." + convert(entry.getKey()) + " = json.opt" + toUpperCase(entry.getValue()) + "(\"" + entry.getKey() + "\");\r\n";
				result += "		}\r\n";
			} else {
				result +=  "		" + name + "." + convert(entry.getKey()) + " = json.opt" + toUpperCase(entry.getValue()) + "(\"" + entry.getKey() + "\");\r\n";
			}
		}
		
		result += "		return " + name + ";\r\n";
		
		result += "	}\r\n";
		result += "}\r\n";
		
		return result;
	}
	
	/**
	 * 去掉首位两个字符
	 * @param str
	 * @return
	 */
	public String clip(String str) {
		String temp = str.trim();
		return temp.substring(1, temp.length() - 1);
	}

	//把首字母大写
	public String toUpperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * 把xx_xx命名(驼峰命名法)变成符合java编程规范的命名xxXx
	 * @param str
	 * @return
	 */
	public String convert(String str) {
		if (str.contains("_")) {
			String arr[] = str.split("_");
			return arr[0] + toUpperCase(arr[1]);
		}
		return str;
	}
}
