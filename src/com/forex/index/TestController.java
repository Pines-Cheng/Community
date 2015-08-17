package com.forex.index;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.forex.util.JSONParser;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.core.Controller;

/**
 * IndexController 测试服务器是否启动成功
 */
@ClearInterceptor
public class TestController extends Controller {

	public void index() {
		renderJson("Starting status", "SUCCESS");
	}

	public void redirect() {
		renderError(HttpServletResponse.SC_FORBIDDEN);
	}

	public void parse() {
		String tableName = getPara("tableName");
		String json = getPara("json");
		try {
			String text = new JSONParser().parse(json, tableName);
			renderText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void manage() {
		redirect("/manage/manage.html");
	}

}
