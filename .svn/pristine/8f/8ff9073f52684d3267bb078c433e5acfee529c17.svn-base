package com.forex.controller.interceptor;

import com.forex.controller.Status;
import com.forex.model.bean.Authority;
import com.forex.model.service.TokenService;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;

public class AuthInterceptor extends PrototypeInterceptor {
	
	private TokenService tokenService = new TokenService();

	@Override
	public void doIntercept(ActionInvocation ai) {
		String token = ai.getController().getPara("token");
		System.out.println("token = " + token);
		if (token == null) {
			ai.getController().renderJson("stat", Status.NO_TOKEN);
			return;
		}
		Authority auth = tokenService.getUserIdByToken(token);
		
		System.out.println("Interceptor token = " + auth.getUserId());
		
		//boolean devMode = Render.getDevMode(); //得到jfinal的开发模式(是否为开发者模式)
		if (auth.getUserId() != null) {
			ai.getController().setAttr("userId", auth.getUserId());
			
			long start = System.currentTimeMillis();
			invoke(ai, auth.getAuthority());
			long end = System.currentTimeMillis();
			
			System.out.println(ai.getActionKey() +  " Spend time mills : " + (end - start));
		} else {
			ai.getController().renderJson("stat", Status.INVALID_TOKEN);
		}
	}

	protected void invoke(ActionInvocation ai, int authority) {
		ai.invoke();
	}

}
