package com.forex.model.bean;

import com.jfinal.plugin.activerecord.Model;

public class TopicReply extends Model<TopicReply> implements Reply{
	
	private static final long serialVersionUID = -4467662931054952935L;

	public static final TopicReply me = new TopicReply();
}
