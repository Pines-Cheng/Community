package com.forex.model.bean;

import com.jfinal.plugin.activerecord.Model;

public class NewsReply extends Model<NewsReply> implements Reply{
	
	private static final long serialVersionUID = -4467662931054952935L;

	public static final NewsReply me = new NewsReply();
}
