package com.forex.model.service;

import java.util.List;

import com.forex.model.bean.Authority;
import com.forex.util.KeyUtil;
import com.forex.util.MD5;
import com.forex.util.TimeUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

public class TokenService {
	
	public static final String CACHE_NAME = "userTokenCache";

	/** 生成用户登录令牌 **/
	public String createToken(long userId, int authority) {
		String key = KeyUtil.createKey(8);
		String token = MD5.encode(userId + "_" + key);
		Record record = Db.findById("user_token", "user_id", userId);
		long time = TimeUtil.getCurrentTime();
		if (record == null) {
			record = new Record().set("user_id", userId).set("token", token)
					.set("update_time", time).set("create_time", time)
					.set("authority", authority);
			Db.save("user_token", record);
		} else {
			record.set("token", token).set("update_time", time)
					.set("authority", authority);
			Db.update("user_token", "user_id", record);
		}
		CacheKit.remove(CACHE_NAME, "USER_CACHE" + userId);
		return token;
	}

	public Authority getUserIdByToken(String token) {
		Authority authority = new Authority();
		String sql = "select user_id,authority from user_token where token=?";
		List<Record> records = Db.findByCache(CACHE_NAME, token, sql, token);
		if (records.size() > 0) {
			authority.setUserId(records.get(0).getLong("user_id"));
			authority.setAuthority(records.get(0).getInt("authority"));
			authority.setToken(token);
		}
		return authority;
	}
	
	public Authority getToken(long userId) {
//		Authority authority = new Authority();
//		String sql = "select token,authority from user_token where user_id=?";
//		List<Record> records = Db.findByCache(CACHE_NAME, "USER_CACHE" + userId, sql, userId);
//		if (records.size() > 0) {
//			authority.setUserId(userId);
//			authority.setAuthority(records.get(0).getInt("authority"));
//			authority.setToken(records.get(0).getStr("token"));
//		}
//		return authority;
		return getToken(userId, true);
	}
	
	public Authority getToken(long userId, boolean inCache) {
		Authority authority = new Authority();
		Record record = null;
		String sql = "select token,authority from user_token where user_id=?";
		if (inCache) {
			List<Record> records = Db.findByCache(CACHE_NAME, "USER_CACHE" + userId, sql, userId);
			if (records != null && records.size() > 0) {
				record = records.get(0);
			} else {
				record = Db.findFirst(sql, userId);
			}
		} else {
			record = Db.findFirst(sql, userId);
		}
		if (record != null) {
			authority.setUserId(userId);
			authority.setAuthority(record.getInt("authority"));
			authority.setToken(record.getStr("token"));
		}
		return authority;
	}
}
