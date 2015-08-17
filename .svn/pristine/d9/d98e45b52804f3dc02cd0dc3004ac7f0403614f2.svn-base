package com.zforex.test;

import org.junit.BeforeClass;
import org.junit.Test;

import com.forex.common.BaseConfig;
import com.forex.model.bean.User;
import com.forex.model.service.GroupService;
import com.forex.model.service.UserService;
import com.forex.util.MD5;
import com.jfinal.ext.test.ControllerTestCase;

public class UserServiceTest extends ControllerTestCase<BaseConfig> {

	private static UserService userService;

	@BeforeClass
	public static void onInit() throws Exception {
		userService = new UserService();
	}

	@Test
	public void test() {
		User user = User.me
				.findFirst("select sum(id) from userinfo where id=100");
		Object obj = user.getAttrValues()[0];
		System.out.println(obj);
	}

	@Test
	public void testGetUserById() {
		System.out.println(userService.getUserById(3));
	}

	@Test
	public void testAddUser() {
		userService.addUser("13787044867", MD5.encode("123456"), "123");
	}

	@Test
	public void testGetUserByLogin() {
		User user = userService.getUser("13787044867", MD5.encode("123456"));
		System.out.println(user);
	}

	@Test
	public void testGetFansNum() {
		System.out.println(userService.getFansCount(1));
	}

	@Test
	public void testMark() {
		userService.mark(1, 2, 3, "Hello,World!");
	}

	@Test
	public void testGetTopUsers() {
		System.out.println(userService.getTopUsers(0));
	}

	@Test
	public void testUpdate() {
		userService.update(6, "simafei", "我投资很牛B哦", true);
	}

	@Test
	public void addGroup() {
		GroupService service = new GroupService();
		service.insertDefault();
	}
}
