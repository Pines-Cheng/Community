package com.forex.controller.manage;

import com.forex.controller.BaseController;
import com.forex.controller.Status;
import com.forex.model.service.ReplyService;

public class ReplyController extends BaseController {

	private ReplyService service = new ReplyService();

	// 删除回复 必须参数token, replyId
	public void delete() {
		long replyId = getParaToLong("replyId");
		if (!service.delete(replyId)) {
			setResult(Status.OPERATE_FAILD, "删除回复失败!");
		}
		renderJson();
	}
}
