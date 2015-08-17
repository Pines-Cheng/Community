package com.forex.controller.manage;

import com.forex.controller.BaseController;
import com.forex.controller.Status;

public class UserController extends BaseController {

	public void login() {

		if (getPara("userName").equals("xinger")
				&& getPara("password").equals("xg123456")) {
			createToken(getPara("userName"));
			// 设置Cookie和Session
			setSessionAttr("manage.userName", getPara("userName")); // 每次都通过userName判断Token是否存在

			renderJson("stat", Status.LOGIN_SUCCESS);

		} else {
			setResult(Status.ERR_TEL_OR_PWD, "用户名或密码错误!");
			renderJson();
		}
	}

	public void logout() {
		if (getSessionAttr("manage.userName") != null) {
			removeSessionAttr("manage.userName");
			renderJson();
		} else {
			setResult(Status.OPERATE_FAILD, "操作失败！");
		}
	}
}
