package com.forex.model.service;

import java.util.List;

import com.forex.model.bean.NewsReply;
import com.forex.model.bean.TopicReply;
import com.forex.util.TimeUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ReplyService {
	
	private static final int Default_Size = 15;
	
	private static final String cancelAgreeSQL = "delete from reply_agree where subject_id=? and user_id=?";
	
	private static final String getTopicReplyListSQL = "select * from topic_reply where attach_id=?";
	private static final String getLimitTopicReplyListSQL = "select * from topic_reply where attach_id=? limit ?,?";
	
	private static final String getNewsReplyListSQL = "select * from news_reply where attach_id=?";
	private static final String getLimitNewsReplyListSQL = "select * from news_reply where attach_id=? limit ?,?";

	private MessageService messageService = new MessageService();

	public TopicReply addReply(long userId, String username, long topicId,
			String content) {
		return addReply(userId, username, null, topicId, 0, content);
	}

	public NewsReply addNewsReply(long userId, String username, String receiver,
			String newsId, long replyId, String content) {
		NewsReply reply = new NewsReply().set("user_id", userId)
				.set("username", username).set("attach_id", newsId).set("content", content)
				.set("sendtime", TimeUtil.getCurrentTime());
		if (replyId > 0) {
			reply.set("reply_id", replyId).set("receiver", receiver);
		}
		if (reply.save()) {
			return reply;
		}
		return null;
	}

	public NewsReply addNewsReply(long userId, String username, String newsId,
			String content) {
		return addNewsReply(userId, username, null, newsId, 0, content);
	}

	public TopicReply addReply(long userId, String username, String receiver,
			long attachId, long replyId, String content) {
		TopicReply reply = new TopicReply().set("user_id", userId)
				.set("username", username).set("attach_id", attachId).set("content", content)
				.set("sendtime", TimeUtil.getCurrentTime());
		if (replyId > 0) {
			reply.set("reply_id", replyId).set("receiver", receiver);
		}
		if (reply.save()) {
			messageService.replyMessage(userId, username, attachId, content);
			return reply;
		}
		return null;
	}
	
	/**得到帖子下面的回复列表**/
	public List<TopicReply> getTopicReply(long topicId){
		return TopicReply.me.find(getTopicReplyListSQL, topicId);
	}
	
	/**得到新闻下面的回复列表**/
	public List<NewsReply> getNewsReply(String newsId) {
		return NewsReply.me.find(getNewsReplyListSQL, newsId);
	}
	
	/** 得到帖子下面的回复列表 **/
	public List<TopicReply> getTopicReply(long topicId, int start, int size) {
		int pagesize = size == 0 ? Default_Size : size;
		return TopicReply.me.find(getLimitTopicReplyListSQL, topicId,
				start, pagesize);
	}

	/** 得到新闻下面的回复列表 **/
	public List<NewsReply> getNewsReply(String newsId, int start, int size) {
		int pagesize = size == 0 ? Default_Size : size;
		return NewsReply.me.find(getLimitNewsReplyListSQL, newsId,
				start, pagesize);
	}
	
	public long[] getNewsAgreeCount(String newsId) {
		String sql = "select count(*) as num, isSupport from news_agree where subject_id=? group by isSupport";
		long[] arr = new long[2];
		for (Record record : Db.find(sql, newsId)) {
			if (record.getBoolean("isSupport")) {
				arr[0] = record.getLong("num");
			} else {
				arr[1] = record.getLong("num");
			}
		}
		return arr;
	}
	
	public int isSupportNews(String newsId, long userId) {
		String sql = "select isSupport from news_agree where subject_id=? and user_id=?";
		Record record = Db.findFirst(sql, newsId, userId);
		if (record == null) {
			return 0;
		} else if (record.getBoolean("isSupport")) {
			return 1;
		} else {
			return -1;
		}
	}
	
	/**给回复点赞**/
	public boolean addAgree(long topicId, long userId) {
		Record record = new Record().set("subject_id", topicId).set("user_id", userId);
		return Db.save("reply_agree", record);
	}
	
	/**取消回复的点赞**/
	public boolean cancelAgree(long topicId, long userId) {
		return Db.update(cancelAgreeSQL, topicId, userId) > 0;
	}
	
	public boolean delete(long replyId) {
		return TopicReply.me.deleteById(replyId);
	}
	
	public boolean deleteNewsReply(long replyId) {
		return NewsReply.me.deleteById(replyId);
	}
	
	public boolean delete(long replyId, long userId) {
		String sql = "delete from topic_reply where id=? and user_id=?";
		return Db.update(sql, replyId, userId) > 0;
	}
	
	public boolean deleteNewsReply(long replyId, long userId) {
		String sql = "delete from news_reply where id=? and user_id=?";
		return Db.update(sql, replyId, userId) > 0;
	}
}
