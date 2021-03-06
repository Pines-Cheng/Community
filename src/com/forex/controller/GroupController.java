package com.forex.controller;

import java.io.File;

import com.forex.controller.interceptor.UploadFileInterceptor;
import com.forex.controller.interceptor.WriteAuthInterceptor;
import com.forex.factory.FileNameFactory;
import com.forex.factory.SaveDirectoryFactory;
import com.forex.model.bean.Group;
import com.forex.model.service.GroupService;
import com.forex.util.ImageHelper;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.upload.UploadFile;

@Before(WriteAuthInterceptor.class)
public class GroupController extends BaseController {

	private GroupService service = new GroupService();

	// 必须参数 token, groupId
	public void get() {
		long groupId = getParaToLong("groupId");
		renderJson("group", service.getGroupById(groupId));
	}
	
	// 必须参数 token, name(群名称), intro(群介绍)
	@ClearInterceptor(ClearLayer.ALL) //带有上传文件的不能用拦截器拦截，因为文件没解析时，拦截器获取不到数据
	@Before(UploadFileInterceptor.class)
	public void add() {
		long userId = getAttr("userId");
		UploadFile file = getAttr("file");;
		
		Group group = service.createGroup(userId, getPara("name"), getPara("intro"));
		if (group != null) {
			if (file != null) {
				String newFileName = FileNameFactory
						.createGroupImageName(group);
				String saveDirectory = SaveDirectoryFactory.getGroupSaveDirectory(file.getSaveDirectory());
				System.out.println(saveDirectory);
				//保存大图
				File destFile = new File(saveDirectory, newFileName);
				file.getFile().renameTo(destFile); 
				//生成缩略图
				String thumbName = FileNameFactory.thumbGroupImageName(group);
				File thumbFile = new File(saveDirectory, thumbName);
				ImageHelper.compressPic(destFile, thumbFile);
				service.updateImagePath(group, SaveDirectoryFactory.Group_Photo_URL_Prefix + thumbName);
			}
			addParam("group", group);
		} else {
			setResult(Status.OPERATE_FAILD, "创建群组失败!");
		}
		renderJson();
	}
	
	public void quitGroup() {
		long userId = getAttr("userId");
		if (!service.quitGroup(userId, getParaToLong("groupId"))) {
			setResult(Status.OPERATE_FAILD, "退出群组失败");
		} 
		renderJson();
	}
	
	// 可选参数 minGrade
	public void list() {
		long userId = getAttr("userId");
		String minGrade = getPara("minGrade");
		if (minGrade == null) {
			renderJson("groups", service.getGroups(userId));
		} else {
			renderJson("groups", service.getGroups(userId, getParaToInt("minGrade")));
		}
	}
	
	public void info() {
		long groupId = getParaToLong("groupId");
		renderJson("group", service.getGroupInfo(groupId));
	}
	
	public void listJoined() {
		long userId = getAttr("userId");
		renderJson("groups", service.getJoinedGroups(userId));
	}
	
	//必须参数 token, groupId, userId（同意谁入群）
	public void join() {
		if (!service.joinGroup(getParaToLong("userId"), getParaToLong("groupId"))) {
			setResult(Status.OPERATE_FAILD, "加入群组失败!");
		}
		renderJson();
	}
	
	//必须参数 token, groupId, reason 可选参数 username, groupName
	public void applyFor() {
		boolean result = false;
		long userId = getAttr("userId");
		long groupId = getParaToLong("groupId");
		String username = getPara("username");
		String groupName = getPara("groupName");
		String reason = getPara("reason");
		if (username != null && groupName != null) {
			result = service.applyForGroup(userId, username, groupId, groupName, reason);
		} else {
			result = service.applyForGroup(userId, groupId, reason);
		}
		if (!result) {
			setResult(Status.OPERATE_FAILD, "加入群组失败!");
		}
		renderJson();
	}
	
	public void insertDefault() {
		service.insertDefault();
		renderJson();
	}
	
}
