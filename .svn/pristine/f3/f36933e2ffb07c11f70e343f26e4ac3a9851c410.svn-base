package com.forex.controller.interceptor;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.ActionInvocation;
import com.jfinal.upload.UploadFile;

public class UploadFileInterceptor extends AuthInterceptor {

	@Override
	public void doIntercept(ActionInvocation ai) {
		List<UploadFile> files = new ArrayList<UploadFile>();
		try {
			files = ai.getController().getFiles();
		} catch(Throwable t) {
			System.out.println("出异常啦!");
		} finally {
			ai.getController().setAttr("files", files);
			ai.getController().setAttr("file", files.size() > 0 ? files.get(0) : null);
			super.doIntercept(ai);
		}
	}

}
