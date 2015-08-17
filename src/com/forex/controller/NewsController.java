package com.forex.controller;

import com.forex.common.Constants;
import com.forex.controller.interceptor.AuthInterceptor;
import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.model.bean.User;
import com.forex.model.service.NewsService;
import com.jfinal.aop.Before;

public class NewsController extends BaseController {
	
	private NewsService service = new NewsService();

	//给回复点赞 必须参数token, replyId
	@Before(WriteAuthInterceptor.class)
	public void addAgree() {
		long userId = getAttr("userId");
		boolean isSupport = getParaToBoolean("isSupport", true);
		if (!service.addAgree(getPara("newsId"), userId, isSupport)) {
			setResult(Status.OPERATE_FAILD, "点赞失败!");
		} 
		renderJson();
	}
	
	//取消回复点赞 必须参数token, replyId
	@Before(WriteAuthInterceptor.class)
	public void cancelAgree(){
		long userId = getAttr("userId");
		if (!service.cancelAgree(getParaToLong("replyId"), userId)) {
			setResult(Status.OPERATE_FAILD, "取消失败!");
		} 
		renderJson();
	}
	
	@Before(AuthInterceptor.class)
	public void recommend() {
		long userId = getAttr("userId");
		int start = getParaToInt("start", 0);
		User user = User.me.findFirst("select device_id from userinfo where id=?", userId);
		redirect(Constants.getRecommendNewsServerUrl() + "?userId=" + user.getStr("device_id") + "&start=" + start);
	}
	
	@Before(AuthInterceptor.class)
	public void find() {
		long userId = getAttr("userId");
		String newsType = getPara("newsType");
		int start = getParaToInt("start", 0);
		int size = getParaToInt("size", 10);
		if ("CALCULATE".equals(newsType)) {
			String url = Constants.getNewsServerUrl2() + "/news/calculateNews";
			String jsonString = service.getNews(userId, url, start, size);
			jsonString = jsonString == null ? "" : jsonString;
			renderJson(jsonString);
		} else if ("SIGNAL".equals(newsType)) {
			String url = Constants.getNewsServerUrl2() + "/news/signalNews";
			String jsonString = service.getNews(userId, url, start, size);
			jsonString = jsonString == null ? "" : jsonString;
			renderJson(jsonString);
		} else if ("CURRENT".equals(newsType) || "NORMAL".equals(newsType)) {
			String url = Constants.getNewsServerUrl2() + "/news/normalNews";
			String jsonString = service.getNews(userId, url, start, size);
			jsonString = jsonString == null ? "" : jsonString;
			renderJson(jsonString);
		} else {
			redirect(Constants.getNewsServerUrl() + "/news/find?newsType=" + newsType + "&start=" + start + "&size=" + size);
		}
	}
}
