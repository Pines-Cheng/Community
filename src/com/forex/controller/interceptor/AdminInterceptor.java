package com.forex.controller.interceptor;

import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;

public class AdminInterceptor extends PrototypeInterceptor {

	@Override
	public void doIntercept(ActionInvocation ai) {
		if ("OK".equals(ai.getController().getSessionAttr("Login"))) {
			ai.invoke();
		} else {
			ai.getController().renderJson("Stat", "Permission not allowed");
		}
	}
}
