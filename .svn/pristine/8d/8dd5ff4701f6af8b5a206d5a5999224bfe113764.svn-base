package com.forex.controller;

import com.forex.controller.interceptor.AuthInterceptor;
import com.forex.model.service.SuggestService;
import com.jfinal.aop.Before;

public class SuggestController extends BaseController {

	private SuggestService service = new SuggestService();
	
	@Before(AuthInterceptor.class)
	public void add() {
		long userId = getAttr("userId");
		String contact = getPara("contact");
		if (!service.addSuggest(userId, getPara("content"), contact)) {
			setResult(Status.OPERATE_FAILD, "发送意见或反馈失败");
		} 
		renderJson();
	}
	
}
