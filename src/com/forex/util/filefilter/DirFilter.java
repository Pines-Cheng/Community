package com.forex.util.filefilter;

import java.io.File;
import java.io.FileFilter;

public class DirFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		// TODO Auto-generated method stub
		if (file.isDirectory()) // 过滤掉目录
			return false;
		else {

			return true;

		}
	}

}
