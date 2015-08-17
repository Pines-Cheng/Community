package com.forex.model.bean;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> {

	private static final long serialVersionUID = -4333143490538076328L;

	public static final User me = new User();
	
	public void clearPwdAndSalt() {
		this.remove("password", "salt");
	}
}
