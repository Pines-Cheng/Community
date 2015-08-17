package com.forex.model.service;

import com.forex.util.TimeUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SuggestService {

	public boolean addSuggest(long userId, String content, String contact) {
		Record record = new Record().set("user_id", userId)
				.set("contact", contact)
				.set("content", content)
				.set("create_time", TimeUtil.getCurrentTime());
		return Db.save("suggest", record);
	}
}
