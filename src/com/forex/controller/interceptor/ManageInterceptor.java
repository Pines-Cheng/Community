package com.forex.controller.interceptor;

import com.forex.controller.Status;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;

public class ManageInterceptor extends PrototypeInterceptor {

	@Override
	public void doIntercept(ActionInvocation ai) {
		String userNameString = ai.getController().getSessionAttr(
				"manage.userName");
		System.out.println(userNameString);

		// 返回值为true则表示Token中已经存在，重复提交
		if (userNameString != null) {
			ai.invoke();
		} else {
			ai.getController().renderJson("stat", Status.INVALID_TOKEN); // 只能调用父类中的方法
		}
	}
}
