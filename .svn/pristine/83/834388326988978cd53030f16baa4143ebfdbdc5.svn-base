package com.forex.controller;

import java.util.List;

import com.forex.controller.interceptor.AuthInterceptor;
import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.model.bean.NewsReply;
import com.forex.model.bean.Reply;
import com.forex.model.bean.TopicReply;
import com.forex.model.service.ReplyService;
import com.jfinal.aop.Before;

public class ReplyController extends BaseController {

	private ReplyService service = new ReplyService();

	//必须参数：token,username,topicId,content 可选 replyId,receiver
	@Before(WriteAuthInterceptor.class)
	public void add() {
		long userId = getAttr("userId");
		String replyId = getPara("replyId");

		TopicReply reply = null;
		if (replyId != null && Long.valueOf(replyId) > 0) {
			reply = service.addReply(userId, getPara("username"),
					getPara("receiver"), getParaToLong("topicId"),
					getParaToLong("replyId"), getPara("content"));
		} else {
			reply = service.addReply(userId, getPara("username"),
					getParaToLong("topicId"), getPara("content"));
		}
		setPostResult(reply);
	}
	
	//必须参数：token,username,newsId,content 可选 replyId,receiver
	@Before(WriteAuthInterceptor.class)
	public void addNewsApply() {
		long userId = getAttr("userId");
		String replyId = getPara("replyId");

		NewsReply reply = null;
		if (replyId != null && Long.valueOf(replyId) > 0) {
			reply = service.addNewsReply(userId, getPara("username"),
					getPara("receiver"), getPara("newsId"),
					getParaToLong("replyId"), getPara("content"));
		} else {
			reply = service.addNewsReply(userId, getPara("username"),
					getPara("newsId"), getPara("content"));
		}
		setPostResult(reply);
	}
	
	@Before(AuthInterceptor.class)
	public void getNewsReplyList() {
		long userId = getAttr("userId");
		int start = getParaToInt("start", 0);
		int size = getParaToInt("size", 0);
		String newsId = getPara("newsId");
		List<NewsReply> replys;
		if (start > 0) {
			replys = service.getNewsReply(newsId, start, size);
		} else {
			replys = service.getNewsReply(newsId);
		}
		long[] counts = service.getNewsAgreeCount(newsId);
		int isSupport = service.isSupportNews(newsId, userId);
		addParam("support", counts[0]);
		addParam("nonSupport", counts[1]);
		addParam("isSupport", isSupport);
		renderJson("replys", replys);
	}
	
	//获取帖子下面的所有回复 必须参数 token, topicId
	@Before(AuthInterceptor.class)
	public void getList() {
		List<TopicReply> replys = service.getTopicReply(getParaToLong("topicId"));
		renderJson("replys", replys);
	}
	
	//给回复点赞 必须参数token, replyId
	@Before(WriteAuthInterceptor.class)
	public void addAgree() {
		long userId = getAttr("userId");
		if (!service.addAgree(getParaToLong("replyId"), userId)) {
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

	//删除回复 必须参数token, replyId
	@Before(WriteAuthInterceptor.class)
	public void delete() {
		long replyId = getParaToLong("replyId");
		if (!service.delete(replyId)) {
			setResult(Status.OPERATE_FAILD, "删除回复失败!");
		}
		renderJson();
	}
	
	private void setPostResult(Reply reply) {
		if (reply != null) {
			addParam("reply", reply);
		} else {
			setResult(Status.OPERATE_FAILD, "发表回复失败!");
		}
		renderJson();
	}
}
