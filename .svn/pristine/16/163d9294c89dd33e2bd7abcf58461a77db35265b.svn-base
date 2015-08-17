package com.forex.model.service;

import java.util.ArrayList;
import java.util.List;

import com.forex.model.bean.Group;
import com.forex.model.bean.Message;
import com.forex.model.bean.Topic;
import com.forex.model.bean.TopicReply;
import com.forex.model.bean.User;
import com.forex.util.TimeUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class MessageService {

	// 删除申请入群的消息
	@Before(Tx.class)
	public boolean delete(long userId, long groupId) {
		Message msg = getMessage(userId, groupId);
		if (msg != null) {
			return Db.update(
					"update user_message set status=2 where message_id=?",
					msg.get("id")); // 此消息已经处理
		} else {
			return false;
		}
	}

	public boolean delete(long msgId) {
		Db.update("delete from user_message where message_id=?", msgId);
		return Message.me.deleteById(msgId);
	}

	public boolean deleteMessage(long msgId, long userId) {
		return Db
				.update("update user_message set status=2 where message_id=? and receiver_id=?",
						msgId, userId) > 0;
	}

	/**
	 * 获得用户申请入群的消息
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public Message getMessage(long userId, long groupId) {
		String sql = "select * from message where send_id=? and attach_id=? and msg_type=1";
		return Message.me.findFirst(sql, userId, groupId);
	}

	/** 申请入群的消息 **/
	@Before(Tx.class)
	public boolean joinGroupMessage(long userId, String username, long groupId,
			String groupName, String reason) {
		String groupSql = "select user_id from groupuser where group_id=? and auth=1"; // 找到群组
		Record record = Db.findFirst(groupSql, groupId);
		long receiverId = (record == null ? 1 : record.getLong("user_id"));

		String title = (username == null ? "用户" + userId : username)
				+ " 向您申请加入圈子 (" + groupName + ")";

		Message msg = getMessage(userId, groupId);

		if (msg != null) {
			// 此消息已经发送过,状态设为0，等待重新发送
			return Db.update(
					"update user_message set status=0 where message_id=?",
					msg.get("id"));
		}

		Message message = new Message().set("send_id", userId)
				.set("username", username).set("msg_type", Message.JOIN_GROUP)
				.set("attach_id", groupId)
				.set("create_time", TimeUtil.getCurrentTime())
				.set("title", title).set("content", reason);

		return saveMessage(message, receiverId);
	}

	/** 申请入群的消息 **/
	@Before(Tx.class)
	public boolean joinGroupMessage(long userId, long groupId, String reason) {
		String username = User.me.findById(userId).getStr("username");
		String groupName = Group.me.findById(groupId).getStr("name");
		return joinGroupMessage(userId, username, groupId, groupName, reason);
	}

	/**
	 * 保存消息
	 * 
	 * @param message
	 *            消息对象
	 * @param receiverId
	 *            消息接收人
	 * @return
	 */
	public boolean saveMessage(Message message, long receiverId) {
		boolean res1 = message.save();
		Record record = new Record().set("receiver_id", receiverId).set(
				"message_id", message.get("id"));
		boolean res2 = Db.save("user_message", record);

		return res1 && res2;
	}

	/** 参与的话题有人回复的消息 **/
	@Before(Tx.class)
	public boolean replyMessage(long userId, String username, long topicId,
			String content) {
		// 通知发帖人
		Topic topic = Topic.me.findById(topicId);
		Long topicUserId = topic.getLong("user_id");
		String tcon = topic.getStr("content");
		if (tcon.length() > 8) {
			tcon = tcon.substring(0, 8) + "...";
		}
		if (topicUserId != userId) {
			Message topicMessage = new Message().set("send_id", userId)
					.set("username", username).set("msg_type", Message.REPLY)
					.set("attach_id", topicId)
					.set("create_time", TimeUtil.getCurrentTime())
					.set("title", username + " 回复了你的帖子(" + tcon + ")")
					.set("content", content);
			if (!saveMessage(topicMessage, topicUserId))
				return false;
		}

		// return noticeAllReplyUser(userId, username, topicId, content,
		// topicUserId, tcon);
		return true;
	}

	public void inviteMessage(Topic topic, String[] userIds) {
		String username = topic.getStr("username");
		String tcon = topic.getStr("content");
		if (tcon.length() > 15) {
			tcon = tcon.substring(0, 15) + "...";
		}
		Message message = new Message()
				.set("send_id", topic.getLong("user_id"))
				.set("username", username).set("msg_type", Message.INVITE)
				.set("attach_id", topic.getLong("id"))
				.set("create_time", TimeUtil.getCurrentTime())
				.set("title", username + "邀请您发表看法")
				.set("content", "原文:" + tcon);

		if (message.save()) {
			String insertSql = "insert into user_message (message_id, receiver_id) values (?,?)";
			Object[][] objects = new Object[userIds.length][2];
			for (int k = 0; k < userIds.length; k++) {
				objects[k][0] = message.get("id");
				objects[k][1] = userIds[k];
			}
			Db.batch(insertSql, objects, userIds.length);
		}
	}

	/**
	 * 
	 * @param userId
	 *            :用户Id
	 * @param username
	 *            :用户名
	 * @param topicId
	 *            :帖子Id
	 * @param content
	 *            :回复内容
	 * @param topicUserId
	 *            :发帖人用户Id
	 * @param tcon
	 *            :帖子内容概要
	 * @return
	 */
	@Deprecated
	public boolean noticeAllReplyUser(long userId, String username,
			long topicId, String content, Long topicUserId, String tcon) {
		// 通知回复人
		String findSql = "select * from topic_reply where attch_id=? and rtype=?";
		List<TopicReply> replys = TopicReply.me.find(findSql, topicId);
		String title = "您回复的帖子(" + tcon + ")有了新回复";

		Message replyMessage = new Message().set("send_id", userId)
				.set("username", username).set("msg_type", 2)
				.set("attach_id", topicId)
				.set("create_time", TimeUtil.getCurrentTime())
				.set("title", title).set("content", content);
		boolean result = replyMessage.save();

		String insertSql = "insert into user_message (message_id, receiver_id) values (?,?)";
		List<Long> waitIds = new ArrayList<Long>(); // 待通知的人
		for (int i = 0; i < replys.size(); i++) {
			Long receiverId = replys.get(i).getLong("user_id");
			if (receiverId != userId/** 不通知自己 **/
			&& !waitIds.contains(receiverId)/** 已经通知过的不通知 **/
			&& receiverId != topicUserId/** 不通知楼主 **/
			) {
				waitIds.add(receiverId);
			}
		}
		int trueLen = waitIds.size();
		if (trueLen > 0) {
			Object[][] objects = new Object[trueLen][2];
			for (int k = 0; k < trueLen; k++) {
				objects[k][0] = replyMessage.get("id");
				objects[k][1] = waitIds.get(k);
			}
			int[] results = Db.batch(insertSql, objects, trueLen);
			int sucResult = 0;
			for (int i = 0; i < results.length; i++) {
				sucResult += results[i];
			}
			return sucResult == trueLen && result;
		}
		return false;
	}

	/**
	 * 获取用户消息列表
	 * 
	 * @param userId
	 * @param readStat
	 *            消息状态 0:未读，2:已读，3:全部
	 * @return
	 */
	@Before(Tx.class)
	public List<Message> getMessages(long userId, int readStat) {
		String where = "";
		switch (readStat) {
		case 0:
			where += " and um.status=0";
			break;
		case 1:
			where += " and um.status=1";
			break;
		case 2:
			where += " and (um.status=0 or um.status=1)";
			break;
		}
		String findSql = "select m.*,um.receiver_id,um.status from message m, user_message um where m.id=um.message_id and receiver_id=? order by m.id desc"
				+ where;
		List<Message> messages = Message.me.find(findSql, userId);
		Db.update("update user_message set status=1 where receiver_id=?",
				userId);
		return messages;
	}

	public long getUnreadMessageCount(long userId) {
		long result = 0;
		String findSql = "select count(*) as num from user_message where receiver_id=? and status=0";
		Record record = Db.findFirst(findSql, userId);
		if (record != null) {
			return record.getLong("num");
		}
		return result;
	}
}
