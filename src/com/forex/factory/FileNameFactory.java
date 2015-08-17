package com.forex.factory;

import com.forex.model.bean.Group;
import com.forex.model.bean.Topic;

public class FileNameFactory {

	public static final String createGroupImageName(Group group) {
		return "G" + group.get("id") + ".jpg";
	}
	
	public static final String thumbGroupImageName(Group group) {
		return "G" + group.get("id") + "S.jpg";
	}
	
	public static final String createTopicImageName(Topic topic, int i) {
		return "T" + topic.get("id") + "B" + i + ".jpg";
	}
	
	public static final String thumbTopicImageName(Topic topic, int i) {
		return "T" + topic.get("id") + "S" + i + ".jpg";
	}
	
	public static final String createUserPhotoName(long userId) {
		return "U" + userId + ".jpg";
	}
	
	public static final String thumbUserPhotoName(long userId) {
		return "U" + userId + "S.jpg";
	}
}
