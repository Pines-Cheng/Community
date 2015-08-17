package com.forex.controller.validator;

import com.forex.controller.BaseController;
import com.forex.controller.Status;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class RegisterValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("tel", "errTel", "用户手机号码不能为空!");
		validateString("password", 6, 32, "errPwd", "用户密码不能为空!");
	}

	@Override
	protected void handleError(Controller c) {
		c.setAttr("stat", Status.VALIDTOR_ERR);
		((BaseController) c).renderAttrJson();
	}

}
