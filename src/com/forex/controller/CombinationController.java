package com.forex.controller;

import java.util.List;
import java.util.Random;

import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.model.service.CombinationService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;

public class CombinationController extends BaseController {
	
	private CombinationService service = new CombinationService();

	@Before(WriteAuthInterceptor.class)
	public void add() {
		long userId = getAttr("userId");
		String name = getPara("combination_name");
		String description = getPara("combination_description");
		int types = getParaToInt("combination_types");
		String currencyTypes = getPara("combination_currency_types");
		int lever = getParaToInt("combination_lever");
		int crashRemaining = getParaToInt("combination_crash_remaining");
		
		//TODO 计算得分，这里先随机生成一个分数
		Random rand = new Random();
		int score = rand.nextInt(100);
		
		boolean result = service.save(userId, name, description, types, currencyTypes, lever, crashRemaining, score);
		if (!result) {
			setResult(Status.OPERATE_FAILD, "存储失败!");
		}
		renderJson();
	}
	
	@Before(WriteAuthInterceptor.class)
	public void list() {
		long userId = getAttr("userId");
		List<Record> records = service.getList(userId);
		renderJson("combines", records);
	}
}
