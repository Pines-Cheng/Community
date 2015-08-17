package com.forex.controller.web;

import com.forex.model.bean.Topic;
import com.forex.model.service.ReplyService;
import com.forex.model.service.TopicService;
import com.forex.util.TimeUtil;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;

@ClearInterceptor(ClearLayer.ALL)
public class WebController extends Controller {

	public void topic() {
		//TODO 加上一个参数 userId
		long topicId = getParaToLong(0);
		TopicService service = new TopicService();
		ReplyService replyService = new ReplyService();
		Topic topic = service.getTopicById(topicId, 0);
		if (topic.getStr("share_code").equals(getPara("shareCode"))) {
			topic.put("timeString", TimeUtil.format(Long.valueOf(topic.getStr("sendtime")) * 1000));
			setAttr("topic", topic);
			setAttr("replys", replyService.getTopicReply(topicId));
		}
	}
	
}
