package com.forex.controller;

import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.model.service.MessageService;
import com.jfinal.aop.Before;

@Before(WriteAuthInterceptor.class)
public class MessageController extends BaseController {
	
	private MessageService service = new MessageService();

	public void get() {
		long userId = getAttr("userId");
		int readStat = getParaToInt("readStat");
		renderJson("messages", service.getMessages(userId, readStat));
	}
	
	public void delete() {
		long userId = getAttr("userId");
		long msgId = getParaToLong("msgId");
		if (!service.deleteMessage(msgId, userId)) {
			setResult(Status.OPERATE_FAILD, "删除失败!");
		}
		renderJson();
	}
}
