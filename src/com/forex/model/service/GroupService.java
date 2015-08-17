package com.forex.model.service;

import java.util.ArrayList;
import java.util.List;

import com.forex.factory.SaveDirectoryFactory;
import com.forex.model.bean.Group;
import com.forex.model.bean.User;
import com.forex.util.TimeUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class GroupService {

	private static final int DEFAULT_LIMIT = 20;
	private static final int MAX_AUTHORITY = 10;
	
	private static final String findPrefix = "select * from groupinfo";
	private static final String findUsersGroupSQL = "select g.*, 'true' as `on` from groupinfo g, groupuser gu where g.id=gu.group_id and gu.user_id=? ";
	private static final String countGroupUsers = "select count(user_id) as num from groupuser where group_id=?";
	
	private MessageService messageService = new MessageService();

	/**创建群**/
	@Before(Tx.class)
	public Group createGroup(long userId, String name, String intro) {
		String username = User.me.findById(userId).get("username", userId + "");
		Group group = new Group().set("creater", username).set("name", name)
				.set("intro", intro)
				.set("create_time", TimeUtil.getCurrentTime());
		if (group.save()) {
			Record record = new Record().set("group_id", group.get("id"))
					.set("user_id", userId).set("auth", 1);
			/** 权限值1为群主，0为普通成员 **/
			if (Db.save("groupuser", record)) {
				return group;
			}
		}
		return null;
	}

	@Before(Tx.class)
	public Group getGroupById(long groupId) {
		Group group = Group.me.findById(groupId);
		long userCount = Db.findFirst(countGroupUsers, group.get("id")).get("num");
		group.put("userCount", userCount);
		return group;
	}
	
	public void updateImagePath(Group group, String imageUrl) {
		group.set("image_path", imageUrl).update();
	}
	
	public Group getGroupByInviteCode(String inviteCode) {
		if (inviteCode == null) return null;
		String findSql = "select id from groupinfo where inviteCode=?";
		return Group.me.findFirst(findSql, inviteCode);
	}
	
	public Group getGroupInfo(long groupId) {
		Group group = getGroupById(groupId);
		//TODO
		return group;
	}
	
	/**获取小于某个等级的群列表,并判断用户是否在群组中**/
	public List<Group> getGroups(long userId, int minGrade) {
		return getGroups(userId, minGrade, DEFAULT_LIMIT);
	}
	
	/**获取小于某个等级的群列表,并判断用户是否在群组中**/
	@Before(Tx.class)
	public List<Group> getGroups(long userId, int minGrade, int limit) {
		String sql = findPrefix + " where grade < ? order by grade desc limit ?";
		List<Group> groups = Group.me.find(sql, minGrade, limit);
		userInGroup(groups, userId);
		countGroupUserNums(groups);
		return groups;
	}
	
	/**获取群组列表,并判断用户是否在群组中**/
	@Before(Tx.class)
	public List<Group> getGroups(long userId) {		
		String sql = findPrefix + " order by grade desc limit ?";
		List<Group> groups = Group.me.find(sql, DEFAULT_LIMIT);
		userInGroup(groups, userId);
		countGroupUserNums(groups);
		return groups;
	}
	
	/**得到用户已经加入的群组**/
	@Before(Tx.class)
	public List<Group> getJoinedGroups(long userId) {
		List<Group> groups = Group.me.find(findUsersGroupSQL, userId);
		List<Group> pubGroups = Group.me.find("select * from groupinfo where id < 13");
		for (Group group : pubGroups) {
//			if (!groups.contains(group)) {
//				groups.add(group);
//			}
			for (Group g2 : groups) {
				if (group.getLong("id").equals(g2.getLong("id"))) {
					groups.add(group);
				}
			}
		}
		countGroupUserNums(groups);
		return groups;
	}
	
	/**得到用户加入的所有群组的ID**/
	public List<Long> getJoinedGroupIds(long userId) {
		List<Long> ids = new ArrayList<Long>();
		String sql = "select group_id from groupuser where user_id=?";
		for (Record record : Db.find(sql, userId)) {
			ids.add(record.getLong("group_id"));
		}
		return ids;
	}
	
	/**申请入群**/
	public boolean applyForGroup(long userId, long groupId, String reason) {
		return messageService.joinGroupMessage(userId, groupId, reason);
	}
	
	/**申请入群**/
	public boolean applyForGroup(long userId,String username, long groupId, String groupName, String reason) {
		return messageService.joinGroupMessage(userId, username, groupId, groupName, reason);
	}
	
	/**加入群组**/
	@Before(Tx.class)
	public boolean joinGroup(long userId, long groupId) {
		Record record = new Record().set("group_id", groupId).set("user_id", userId);
		messageService.delete(userId, groupId);
		return Db.save("groupuser", record);
	}
	
	@Before(Tx.class)
	public boolean quitGroup(long userId, long groupId) {
		Record record = Db.findFirst("select * from groupuser where user_id=? and group_id=?", userId, groupId);
		String deleteSql = "delete from groupuser where user_id=? and group_id=?";
		Db.update(deleteSql, userId, groupId);
		
		if (record.getInt("auth") == 1) {
			//如果是群主退出，则把群交个下一任(经验最高的人)管理,如果没人了，则删除群组
			record = Db.findFirst("select * from groupuser where group_id=? order by exp limit 1", groupId);
			if (record == null) { //如果群里没用户了，则删除这个群组
				Group.me.deleteById(groupId);
				TopicService topicService = new TopicService();
				topicService.deleteGroupsTopic(groupId);
			} else {
				record.set("auth", 1);
				Db.update("groupuser", record);
			}
		}
		return true;
	}
	
	private static String[] arr = new String[]{"美元","欧元","人民币","澳元","英镑","加元","日圆","新西兰元","瑞士法郎","白银","黄金","石油"};
	public void insertDefault() {
		long time = TimeUtil.getCurrentTime();
		String insertSql = "insert into groupinfo (id, creater, name, create_time, image_path) values(?,?,?,?,?)";
		Object[][] params = new Object[arr.length][5];
		for (int i = 0; i < arr.length; i++) {
			params[i][0] = i + 1;
			params[i][1] = "大鱼财经";
			params[i][2] = arr[i];
			params[i][3] = time;
			params[i][4] = SaveDirectoryFactory.Group_Photo_URL_Prefix + "G" + (i + 1) + "S.png";
		}
		Db.batch(insertSql, params, arr.length);
	}
	
	public void joinGroupAccordToInterest(long userId, String[] interests) {
		for (String tag : interests) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains(tag)) {
					Record record = new Record().set("group_id", i + 1).set("user_id", userId);
					Db.save("groupuser", record);
				}
			}
		}
	}
	
	@Before(Tx.class)
	public boolean userInGroup(long groupId, long userId) {
		String sql = "select group_id from groupuser where user_id=? and group_id=?";
		Record record = Db.findFirst(sql, userId, groupId);
		
		String sql2 = "select id from userinfo where id=? and grade>?";
		Record record2 = Db.findFirst(sql2, userId, MAX_AUTHORITY);
		return (record != null) || (record2 != null);
	}
	
	/**计算群组每个群的人数，并作为每个群的成员变量**/
	@Before(Tx.class)
	private void countGroupUserNums(List<Group> groups) {
		for (Group group : groups) {
			Record record = Db.findFirst(countGroupUsers, group.get("id"));
			group.put("num", record.getLong("num"));
		}
	}
	
	/**判断用户是否已经加入了某个群，并把结果存入每个群组的成员变量**/
	@Before(Tx.class)
	private void userInGroup(List<Group> groups, long userId) {
		String sql = "select group_id from groupuser where user_id=?";
		for (Group group : groups) {
			for (Record record : Db.find(sql, userId)) {
				if (group.getLong("id") == record.getLong("group_id")) {
					group.put("on", true);
					break;
				}
				group.put("on", false);
			}
		}
	}

}
