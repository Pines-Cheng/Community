package com.forex.controller.interceptor;

import com.forex.controller.Status;
import com.forex.model.bean.Authority;
import com.jfinal.core.ActionInvocation;

public class WriteAuthInterceptor extends AuthInterceptor {

	@Override
	protected void invoke(ActionInvocation ai, int authority) {
		if (Authority.Write == authority) {
			ai.invoke();
		} else {
			ai.getController().renderJson("stat", Status.NO_PERMISSION);
		}
	}
}
