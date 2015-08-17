package com.forex.model.bean;

import com.jfinal.plugin.activerecord.Model;

public class Message extends Model<Message> {

	private static final long serialVersionUID = 8480707562587841083L;

	public static final Message me = new Message();
	
	public static final int JOIN_GROUP = 1;
	public static final int REPLY = 2;
	public static final int INVITE = 3;
	
}
