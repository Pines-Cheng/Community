package com.forex.factory;

import java.io.File;

import com.forex.common.Constants;

public class SaveDirectoryFactory {
	
	private static boolean isFirstLoad = true;
	
	public static final String User_Photo_URL_Prefix = "/upload/U/";
	public static final String Group_Photo_URL_Prefix = "/upload/G/";

	public static void initSaveDirectory(String root) {
		File file = new File(root);
		if (!file.exists()) {
			file.mkdirs();
		}
		mkdir(root, "U"); //用户头像保存文件夹
		mkdir(root, "G"); //战队头像保存文件夹
		mkdir(root, "T"); //帖子头像保存文件夹
	}
	
	public static void mkdir(String parent, String name) {
		File gFile = new File(parent, name);
		if (!gFile.exists()) {
			gFile.mkdir();
		}
	}
	
	public static String getGroupSaveDirectory(String root) {
		String dir = Constants.SaveDirectory == null ? root : Constants.SaveDirectory;
		isFirstLoad(root);
		return dir + "G";
	}
	
	public static String getUserSaveDirectory(String root) {
		String dir = Constants.SaveDirectory == null ? root : Constants.SaveDirectory;
		isFirstLoad(root);
		return dir + "U";
	}
	
	public static String getTopicSaveDirectory(String root) {
		String dir = Constants.SaveDirectory == null ? root : Constants.SaveDirectory;
		isFirstLoad(root);
		return dir + "T";
	}

	private static void isFirstLoad(String root) {
		if (isFirstLoad) {
			initSaveDirectory(root);
			isFirstLoad = false;
		}
	}
}
