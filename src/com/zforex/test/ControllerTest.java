package com.zforex.test;

import org.junit.Test;

import com.forex.common.BaseConfig;
import com.forex.util.MD5;
import com.jfinal.ext.test.ControllerTestCase;

public class ControllerTest extends ControllerTestCase<BaseConfig> {

	public static final String Test_Token = "3cf93e3196df062fd8d79ccc72a5695f";

	@Test
	public void test() {
		System.out.println(use("/news/recommend?token=" + Test_Token).invoke());
	}

	@Test
	public void testGetUserTopic() {
		// ControllerTestCase<BaseConfig> test = use("/topic/getList");
		// String result = test.post("token = " + Test_Token+ "}").invoke();
		String result = use("/topic/getList?token=" + Test_Token).invoke();
		System.out.println(result);
	}

	@Test
	public void testRegister() {
		String url = "/user/register?tel=111&password=123456&code=123456";
		System.out.println(use(url).invoke());
	}

	@Test
	public void testLogin() {
		String url = "/user/login?tel=15557195707&password="
				+ MD5.encode("123456");
		System.out.println(use(url).invoke());
	}

	@Test
	public void testGetGroupList() {
		String url = "/group/list?token=" + Test_Token;
		System.out.println(use(url).invoke());
	}

	@Test
	public void testCreateGroup() {
		String url = "/group/add?token=" + Test_Token
				+ "&name=愤怒的小鸟&intro=哈哈，呵呵";
		System.out.println(use(url).invoke());
	}

	@Test
	public void testJoinGroup() {
		String url = "/group/join?token=" + Test_Token + "&userId=2&groupId=4";
		System.out.println(use(url).invoke());
	}

	@Test
	public void getReply() {
		String url = "/reply/getList?token=" + Test_Token + "&topicId=9";
		System.out.println(use(url).invoke());
	}

	@Test
	public void getValiImage() {
		String url = "/image/get";
		System.out.println(use(url).invoke());
	}

	@Test
	public void getTopicgetAll() {
		String url = "/manage/topic/getPaginate?pageNumber=0&pageSize=5";
		System.out.println(use(url).invoke());
	}

}
