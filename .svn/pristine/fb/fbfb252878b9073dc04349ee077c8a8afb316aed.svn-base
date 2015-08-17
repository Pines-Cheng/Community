package com.forex.model.service;

import java.util.List;

import com.forex.util.TimeUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CombinationService {
	
	public boolean save(long userId, String name, String description,
			int analysisTypes, String currencyTypes, int lever,
			int crashRemaning, int score) {
		long time = TimeUtil.getCurrentTime();
		Record record = new Record().set("name", name).set("create_time", time)
				.set("modify_time", time).set("user_id", userId)
				.set("description", description)
				.set("analysis_types", analysisTypes)
				.set("crash_remaning", crashRemaning).set("score", score)
				.set("currency_types", currencyTypes).set("lever", lever);
		return Db.save("combination", record);
	}
	
	public List<Record> getList(long userId) {
		List<Record> records = Db.find("select * from combination where user_id=? order by score desc", userId);
		return records;
	}
	
	public void delete(long userId, long combinationId) {
		Db.update("delete from combination where user_id=? and id=?", userId, combinationId);
	}
}
