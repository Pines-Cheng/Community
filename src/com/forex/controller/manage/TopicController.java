package com.forex.controller.manage;

import java.util.List;
import java.util.Map;

import com.forex.controller.BaseController;
import com.forex.controller.Status;
import com.forex.model.bean.Topic;
import com.forex.model.service.ReplyService;
import com.forex.model.service.TopicService;

//@Before(ManageInterceptor.class)
public class TopicController extends BaseController {

	private TopicService service = new TopicService();

	/**
	 * 通过userId,id（topic的id获取topic的详细信息及回复） 必须参数 userId,id
	 * 
	 */
	public void get() {
		long userId = getParaToLong("userId");
		long topicId = getParaToLong("topicId");

		// renderJson("topic", service.getTopicById(getParaToLong("id")));
		ReplyService replyService = new ReplyService();
		addParam("topic", service.getTopicById(topicId, userId));
		addParam("replys", replyService.getTopicReply(topicId));
		renderJson();
	}

	/**
	 * 通过分页获取topic的详细信息
	 * 
	 * @param pageNumber
	 *            必选 当前页
	 * @param pageSize
	 *            若为null，默认为10；
	 */
	public void getPaginate() {
		List<Topic> topics = null;
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");

		if (pageSize == null) {
			pageSize = 10;
		}
		if (pageNumber == null) {
			setResult(Status.INVALID_ARGUMENT, "pageNumber不可为空！");
			renderJson();
			return;
		}
		topics = service.getPaginateTopics(pageNumber, pageSize);
		for (Topic topic : topics) {
			Long userId = topic.getLong("user_id");
			service.addLinkData(topic, userId);
			addParam("topics", topics);

		}
		addParam("topicSum", service.getTopicSum());
		addParam("pageCount",
				Math.ceil(((double) service.getTopicSum() / (double) pageSize)));
		renderJson();
	}

	public void getPaginateByUserId() {

	}

	/**
	 * 通过userId获取topic的链表，
	 * 
	 * @param userId
	 *            必选
	 * @param groupId
	 *            可选(通过组id获取topic信息)
	 * @param minTopicId
	 *            可选(是否接着上一个从最新的topic开始返回,供分页使用，默认每次返回10个)
	 */
	public void getList() {
		long userId = getAttr("userId");
		String minString = getPara("minTopicId");
		List<Topic> topics = null;
		if (getPara("groupId") == null) {
			if (getPara("userId") != null) {
				userId = getParaToLong("userId");
			}
			topics = getUserTopic(userId, minString == null);
		} else {
			topics = getGroupTopic(minString == null);
		}
		renderJson("topics", topics);
	}

	/**
	 * 通过topicid获取topic的详细信息
	 * 
	 * @param topicid
	 *            必选
	 */
	public void getTopicById() {
		String topicIdString = getPara("topicId");
		System.out.println(topicIdString);
		renderJson();
	}

	/**
	 * 通过topicId删除topic
	 * 
	 * @param topicId
	 *            必选
	 */
	public void deleteTopicByTopicId() {
		String topicIdString = getPara("topicId");
		if (topicIdString == null) {
			setResult(Status.INVALID_ARGUMENT, "topicId不可为空！");
			renderJson();
			return;
		}
		service.delete(Long.parseLong(topicIdString));
		addParam("topicid", topicIdString);
		renderJson();

	}

	/**
	 * 通过topicId[]数组删除多喝topic
	 * 
	 * @param topicId
	 *            [] 必选
	 */
	public void deleteTopics() {
		Map<String, String[]> topicIdList = getParaMap();

		String[] topicIdStrings = topicIdList.get("topicIdList[]");
		if (topicIdStrings == null) {
			setResult(Status.INVALID_ARGUMENT, "topicIdList不可为空！");
			renderJson();
			return;
		}
		for (int i = 0; i < topicIdStrings.length; i++) {
			service.delete(Long.parseLong(topicIdStrings[i]));
			// System.out.println(topicIdStrings[i]);
		}
		addParam("topicIdList", topicIdStrings);
		renderJson();

	}

	/**
	 * 通过groupId获取帖子
	 * 
	 * @param isNew
	 *            是否从上次的分页开始往下刷新（默认10条）
	 * @return
	 */
	private List<Topic> getGroupTopic(boolean isNew) {
		long userId = getAttr("userId");
		if (isNew) {
			return service.getNewGroupTopic(getParaToLong("groupId"), userId);
		} else {
			return service.getMoreGroupTopic(getParaToLong("groupId"),
					getParaToInt("minTopicId"), userId);
		}

	}

	/**
	 * 通过userId获取帖子
	 * 
	 * @param userId
	 * @param isNew
	 *            true：获取最新的 false:从上次的分页开始往下刷新（默认10条）
	 * @return
	 */
	private List<Topic> getUserTopic(long userId, boolean isNew) {
		// long id = getAttr("userId");
		// boolean visible = (userId == id); //如果不是自己，不能查看其他用户的战队帖子
		boolean visible = true; // 前期默认可以查看用户战斗帖子
		if (isNew) {
			return service.getNewUserTopic(userId, visible);
		} else {
			return service.getMoreUserTopic(userId, getParaToInt("minTopicId"),
					visible);
		}

	}

}
