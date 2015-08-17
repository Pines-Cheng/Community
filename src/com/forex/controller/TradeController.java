package com.forex.controller;

import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.model.service.NewsService;
import com.jfinal.aop.Before;

@Before(WriteAuthInterceptor.class)
public class TradeController extends BaseController {

	private NewsService service = new NewsService();

	public void buy() {
		long userId = getAttr("userId");
		boolean result = service.tradeWithNews(userId, getPara("type"),
				getPara("newsIds"), getParaToBoolean("isBuy"));
		if (!result) {
			setResult(Status.OPERATE_FAILD, "添加失败!");
		}
		renderJson();
	}
}
