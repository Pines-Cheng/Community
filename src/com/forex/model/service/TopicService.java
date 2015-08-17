package com.forex.model.service;

import java.util.ArrayList;
import java.util.List;

import com.forex.model.bean.Topic;
import com.forex.model.bean.User;
import com.forex.util.KeyUtil;
import com.forex.util.TimeUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class TopicService {

	private static final int DEFAULT_LIMIT = 10;

	private static final String cancelAgreeSQL = "delete from topic_agree where subject_id=? and user_id=?";
	public static final String rcSql = "select count(*) as num from topic_reply where attach_id=?";
	public static final String acSql = "select count(*) as num from topic_agree where subject_id=?";
	public static final String scSql = "select count(*) as num from topic where origin_id=?";
	public static final String rUsersSql = "select id, username from userinfo where id in (%s)";

	/**
	 * 数据库中添加一条帖子
	 * 
	 * @param userId
	 * @param username
	 * @param groupId
	 * @param content
	 * @param isShare
	 *            是否转载
	 * @param originId
	 * @param visible
	 * @param imageNum
	 * @return
	 */
	@Before(Tx.class)
	public Topic addTopic(long userId, String username, long groupId,
			String content, boolean isShare, long originId, String visible,
			int imageNum, String rUserIds) {
		Topic topic = new Topic().set("user_id", userId)
				.set("username", username).set("content", content)
				.set("sendtime", TimeUtil.getCurrentTime())
				.set("share_code", KeyUtil.createKey(6))
				.set("visible", visible).set("image_num", imageNum);
		if (groupId > 0) {
			topic.set("group_id", groupId);
		}
		if (isShare) {
			topic.set("is_share", isShare).set("origin_id", originId);
		}
		if (rUserIds != null && !"".equals(rUserIds)) {
			StringBuffer ids = new StringBuffer(rUserIds);
			if (rUserIds.startsWith(",")) {
				ids.deleteCharAt(0);
			}
			if (rUserIds.endsWith(",")) {
				ids.deleteCharAt(ids.length() - 1);
			}
			topic.set("receiver_users", ids.toString());
		}
		if (topic.save()) {
			if (rUserIds != null && !"".equals(rUserIds)) {
				MessageService mService = new MessageService();
				mService.inviteMessage(topic, rUserIds.split(","));
			}
			return topic;
		}
		return null;
	}

	public Topic addGroupTopic(long userId, String username, long groupId,
			String content, int imageNum, String rUserIds) {
		return addTopic(userId, username, groupId, content, false, 0, "Group",
				imageNum, rUserIds);
	}

	public Topic addUserTopic(long userId, String username, String content,
			int imageNum) {
		return addTopic(userId, username, 0, content, false, 0, "ALL",
				imageNum, null);
	}

	public Topic addReprintTopic(long userId, String username, String content,
			long topicId) {
		Topic topic = Topic.me.findById(topicId);
		if (topic.getBoolean("is_share")) {
			long originId = topic.getLong("origin_id");
			topic = Topic.me.findById(originId);
		}
		return addTopic(userId, username, 0, content, true, topicId, "ALL",
				topic.getInt("image_num"), null);
	}

	/**
	 * 查询小于某个帖子Id的所有用户帖子
	 * 
	 * @param userId
	 *            ：用户Id
	 * @param lastMinId
	 *            ：帖子Id
	 * @param limit
	 *            ：查询多少条记录
	 * @return
	 */
	@Before(Tx.class)
	public List<Topic> getMoreUserTopic(long userId, long lastMinId, int limit,
			boolean visibleAll) {
		List<Topic> topics = null;
		String visible = visibleAll ? "" : " and visible='ALL'";
		if (lastMinId > 0) {
			// String sql =
			// "select * from topic where user_id=? and visible='ALL' and id < ? order by id desc limit ?";
			String sql = "select * from topic where user_id=? " + visible
					+ " and id < ? order by id desc limit ?";
			topics = Topic.me.find(sql, userId, lastMinId, limit);
		} else {
			// String sql =
			// "select * from topic where user_id=? and visible='ALL' order by id desc limit ?";
			String sql = "select * from topic where user_id=? " + visible
					+ " order by id desc limit ?";
			topics = Topic.me.find(sql, userId, limit);
		}
		getTopicLinkData(topics, userId);
		return topics;
	}

	public List<Topic> getCombinationTopic(long userId, long combinationId) {
		List<Topic> topics = Topic.me
				.find("select * from topic where visible = 'Private' and user_id=? and group_id=?",
						userId, combinationId);
		getTopicLinkData(topics, userId);
		return topics;
	}

	/**
	 * 查询最新的帖子数
	 * 
	 * @param userId
	 *            ：用户Id
	 * @param lastMaxId
	 *            ：帖子Id
	 * @param limit
	 *            ：查询多少条记录
	 * @return
	 */
	@Before(Tx.class)
	public List<Topic> getNewUserTopic(long userId, int limit,
			boolean visibleAll) {
		return getMoreUserTopic(userId, 0, DEFAULT_LIMIT, visibleAll);
	}

	public List<Topic> getNewUserTopic(long userId, boolean visibleAll) {
		return getNewUserTopic(userId, DEFAULT_LIMIT, visibleAll);
	}

	public List<Topic> getMoreUserTopic(long userId, long lastMinId,
			boolean visibleAll) {
		return getMoreUserTopic(userId, lastMinId, DEFAULT_LIMIT, visibleAll);
	}

	public Topic getTopicById(long id, long userId) {
		Topic topic = Topic.me.findById(id);
		return addLinkData(topic, userId);
	}

	/**
	 * 查询大于某个帖子Id的所有群组帖子
	 * 
	 * @param groupId
	 *            ：群组Id
	 * @param lastMaxId
	 *            ：帖子Id
	 * @param limit
	 *            ：查询多少条记录
	 * @return
	 */
	public List<Topic> getNewGroupTopic(long groupId, int limit, long userId) {
		return getMoreGroupTopic(groupId, 0, limit, userId);
	}

	public List<Topic> getMoreGroupTopic(long groupId, long lastMinId,
			int limit, long userId) {
		List<Topic> topics = null;
		if (lastMinId > 0) {
			String sql = "select * from topic where group_id=? and (visible='ALL' or visible='GROUP') and id < ? order by id desc limit ?";
			topics = Topic.me.find(sql, groupId, lastMinId, limit);
		} else {
			String sql = "select * from topic where group_id=? and (visible='ALL' or visible='GROUP') order by id desc limit ?";
			topics = Topic.me.find(sql, groupId, limit);
		}
		getTopicLinkData(topics, userId);
		return topics;
	}

	public List<Topic> getNewGroupTopic(long groupId, long userId) {
		return getNewGroupTopic(groupId, DEFAULT_LIMIT, userId);
	}

	public List<Topic> getMoreGroupTopic(long groupId, long lastMinId,
			long userId) {
		return getMoreGroupTopic(groupId, lastMinId, DEFAULT_LIMIT, userId);
	}

	/**
	 * 获取所有用户的帖子
	 * 
	 * @param minTopicId
	 *            前一组查询帖子的最小Id
	 * @return 当(minTopicId>0) 返回id小于minTopicId的一组帖子，当(minTopicId<=0)返回最新的一组帖子
	 */
	@Before(Tx.class)
	public List<Topic> getMoreTopics(long minTopicId, long userId) {
		List<Topic> topics = null;
		if (minTopicId > 0) {
			String sql = "select * from topic where visible='ALL' and id < ? order by id desc limit ?";
			topics = Topic.me.find(sql, minTopicId, DEFAULT_LIMIT);
		} else {
			String sql = "select * from topic where visible='ALL' order by id desc limit ?";
			topics = Topic.me.find(sql, DEFAULT_LIMIT);
		}
		getTopicLinkData(topics, userId);
		return topics;
	}

	/**
	 * 获取topic的总数
	 * 
	 * @return 所有topic的总数
	 */
	public long getTopicSum() {
		String sql = "SELECT count(*) as count from topic ";
		return Db.find(sql).get(0).getLong("count");

	}

	/**
	 * 通过页数和每页的记录数获取List<Topic>,通过时间降序排列
	 * 
	 * @param pageNumber
	 *            第几页
	 * @param pageSize
	 *            每一页的记录数
	 * @return List<Topic>
	 */
	public List<Topic> getPaginateTopics(int pageNumber, int pageSize) {
		String sql = "select * from topic order by sendtime desc  limit ?,?";
		return Topic.me.find(sql, pageNumber, pageSize);
	}

	/**
	 * 通过页数、每页的记录数、userId获取分页的数据List<Topic>
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public List<Topic> getPaginateTopicsByUserId(long userId, int pageNumber,
			int pageSize) {

		String sql = "select * from topic where user_id=? order by sendtime desc  limit ?,?";
		return Topic.me.find(sql, pageSize, userId, pageNumber);

	}

	public List<Topic> getNewTopics(long userId) {
		return getMoreTopics(0, userId);
	}

	// 获得用户所有点过赞的帖子Id
	public List<Long> getSupportTopicIds(long userId) {
		List<Long> ids = new ArrayList<Long>();
		String sql = "select subject_id from topic_agree where user_id=?";
		List<Record> records = Db.find(sql, userId);

		for (Record record : records) {
			ids.add(record.getLong("subject_id"));
		}
		return ids;
	}

	public long[] getTopicAgreeCount(long topicId) {
		String sql = "select count(*) as num, isSupport from topic_agree where subject_id=? group by isSupport";
		long[] arr = new long[2];
		for (Record record : Db.find(sql, topicId)) {
			if (record.getBoolean("isSupport")) {
				arr[0] = record.getLong("num");
			} else {
				arr[1] = record.getLong("num");
			}
		}
		return arr;
	}

	public boolean addAgree(long topicId, long userId) {
		Record record = new Record().set("subject_id", topicId).set("user_id",
				userId);
		return Db.save("topic_agree", record);
	}

	public boolean addAgree(long topicId, long userId, boolean isSupport) {
		Record record = new Record().set("subject_id", topicId)
				.set("user_id", userId).set("isSupport", isSupport);
		return Db.save("topic_agree", record);
	}

	public boolean cancelAgree(long topicId, long userId) {
		return Db.update(cancelAgreeSQL, topicId, userId) > 0;
	}

	/**
	 * 通过topicId删除帖子及评论
	 * 
	 * @param topicId
	 * @return
	 */
	@Before(Tx.class)
	public boolean delete(long topicId) {
		Topic.me.deleteById(topicId);
		Db.update("delete from topic_reply where attach_id=?", topicId);
		return true;
	}

	/**
	 * 通过topicId及userId删除帖子及评论
	 * 
	 * @param topicId
	 * @param userId
	 * @return
	 */
	@Before(Tx.class)
	public boolean delete(long topicId, long userId) {
		String sql = "delete from topic where id=? and user_id=?";
		int result = Db.update(sql, topicId, userId);
		if (result > 0) {
			Db.update("delete from topic_reply where attach_id=?", topicId);
			return true;
		}
		return false;

	}

	@Before(Tx.class)
	public boolean deleteGroupsTopic(long groupId) {
		List<Topic> topics = Topic.me.find(
				"select id from topic where group_id=?", groupId);
		if (topics.size() > 0) {
			StringBuffer set = new StringBuffer();
			for (Topic topic : topics) {
				set.append(topic.getLong("id")).append(",");
			}
			set.deleteCharAt(set.length() - 1);
			Db.update("delete from topic_reply where attach_id in (" + set
					+ ")");
			Db.update("delete from topic where group_id=?", groupId);
		}
		return true;
	}

	/**
	 * 得到topic相关的数据，包括：回复人数，点赞人数，转载人数
	 * 
	 * @param topics
	 */
	@Before(Tx.class)
	private void getTopicLinkData(List<Topic> topics, long userId) {
		for (Topic topic : topics) {
			addLinkData(topic, userId);
		}
	}

	public Topic addLinkData(Topic topic, long userId) {
		long id = topic.getLong("id");
		long replyCount = Db.findFirst(rcSql, id).getLong("num");
		// long agreeCount = Db.findFirst(acSql, id).getLong("num");
		long shareCount = Db.findFirst(scSql, id).getLong("num");
		Boolean isSupport = topicIsSupportedByUser(topic, userId);

		long[] counts = getTopicAgreeCount(topic.getLong("id"));

		String rUserIds = topic.getStr("receiver_users");
		if (rUserIds != null && !"".equals(rUserIds)) {
			String sql = String.format(rUsersSql, rUserIds);
			System.out.println(sql);
			topic.put("rUsers", User.me.find(sql));
		}

		topic.put("replyCount", replyCount).put("agreeCount", counts[0])
				.put("disagreeCount", counts[1]).put("shareCount", shareCount)
				.put("isSupport", isSupport);
		return topic;
	}

	/**
	 * 用户是否对该帖子点过赞
	 * 
	 * @param topic
	 * @param userId
	 * @return
	 */
	private Boolean topicIsSupportedByUser(Topic topic, long userId) {
		String findSql = "select isSupport from topic_agree where subject_id=? and user_id=?";
		Record record = Db.findFirst(findSql, topic.get("id"), userId);
		if (record == null) {
			return null;
		} else {
			return record.getBoolean("isSupport");
		}
	}
}
