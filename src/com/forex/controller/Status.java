package com.forex.controller;

public enum Status {
	OK, /** 操作成功 **/
	ERR_TEL_OR_PWD, /** 无效的用户名或密码 **/
	OPERATE_FAILD, /** 操作数据到数据库中出错 **/
	QUERY_FAILD, /** 查询数据失败 **/
	NO_TOKEN, /** 没有身份令牌 **/
	INVALID_TOKEN, /** 无效的身份令牌 **/
	NO_PERMISSION, /** 没有相关权限 **/
	SERVER_EXCEPTION, /** 服务器异常 **/
	INVALID_ARGUMENT, /** 无效的参数 **/
	VALIDTOR_ERR, /** 验证失败 **/
	MSG_SEND_ERR, /** 短信发送失败 **/
	INVALID_MSG_CODE, /** 短信验证码验证失败 **/
	ILLEGAL_STATE, /** 不合法的状态 **/
	EMPTY_FILE
	/** 文件为空 **/
	, LOGIN_SUCCESS
	/** 登录成功 **/
}
