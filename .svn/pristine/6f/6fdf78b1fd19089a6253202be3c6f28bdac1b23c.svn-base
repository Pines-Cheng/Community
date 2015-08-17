package com.forex.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.core.Controller;

public class BaseController extends Controller {

	protected Status stat = Status.OK; // action执行状态
	protected String errText; // 错误信息，如果执行成功，该值为空

	private Map<String, Object> returnParams = new HashMap<String, Object>();// 返回的MAP

	/**
	 * 添加返回的参数
	 */
	protected void addParam(String key, Object value) {
		returnParams.put(key, value);
	}

	/**
	 * 设置返回的通用的处理结果
	 * 
	 * @param stat
	 *            处理结果状态
	 * @param errText
	 *            处理结果错误信息
	 */
	protected void setResult(Status stat, String errText) {
		this.stat = stat;
		this.errText = errText;
	}

	@Override
	public void renderJson(String key, Object object) {
		addParam(key, object);
		renderJson();
	}

	@Override
	public void renderJson() {
		Map<String, Object> map = createBaseMap();
		map.putAll(returnParams);
		clear();
		renderJson(map);
	}

	public void renderAttrJson() {
		super.renderJson();
	}

	private Map<String, Object> createBaseMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stat", stat);
		if (errText != null && !"".equals(errText)) {
			map.put("errText", errText);
		}
		return map;
	}

	private void clear() {
		returnParams.clear(); // 清空一下这个Map，Controller有可能是单例，之前的结果会产生影响
		stat = Status.OK;
		errText = null;
	}
}
