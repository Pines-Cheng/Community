<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<style type="text/css">
			.trendBox {
				width: 94%;
				padding-top: 20px;
				margin: 0 auto;
				line-height: 100%;
			}
			.userPhoto {
				width: 40px;
				height: 40px;
				float: left;
				border: 1px solid #EEEEEE;
				text-align: center;
			}
			.userInfo {
				width: 80%;
				margin-left: 50px;
				height: 40px;
			}
			.content {
				clear: left;
				margin-top: 10px;
			}
			.option {
				margin-top: 10px;
				width: 100%;
				height: 40px;
				background: url(/img/topic_opt_bg.png) no-repeat;
				background-size: 100% 100%;
			}
			.option label {
				width: 12%;text-align:center; display: block; line-height: 40px; float: left;
			}
			.replyContent {
				margin-top: 10px;
			}
		</style>
	</head>
	<body>
		<div class="trendBox">
			<div class="userPhoto">
				<img src="/upload/U/U${topic.user_id}.jpg" onerror="this.src='/img/niu.png'" width="90%" style="margin-top: 5%;" />
			</div>
			<div class="userInfo">
				<label>${topic.username }</label><br />
				<label style="color: #858585; font-size: 14px; margin-top: 12px; display: block;">${topic.timeString }</label>
			</div>
			<div class="content">
				${topic.content }
			</div>
			<div class="option">
				<label style="margin-left: 3%;">评论</label>
				<label style="margin-right: 3%;">${topic.replyCount }</label>
				<label style="margin-left: 20%;">转发</label>
				<label style="">${topic.shareCount }</label>
				<label style="margin-left: 2%;">点赞</label>
				<label style="">${topic.agreeCount }</label>
			</div>
			
			<c:forEach var="reply" items="${replys }">
			<div class="replyContent">
				<div class="userPhoto" style="margin-right: 10px;">
					<img src="/upload/U/U${reply.user_id}.jpg" onerror="this.src='/img/niu.png'" width="90%" style="margin-top: 5%;" />
				</div>
				<div class="replyInfo">
					<label>${reply.username }</label><br />
					<label style="font-size: 15px;margin-left: 50px; margin-top: 10px; display: block;">
						${reply.content }
					</label>
				</div>
				<img src="/img/separator_line.png" width="100%" height="1px" />
			</div>
			</c:forEach>
			
			<!-- 
			.bottom {
				background: url(/img/option_bg.png);
				height: 40px;
				width: 100%;
				background-size: 100% 100%;
			}
			.bottom a {
				width: 33%;
				display: block;
				float: left;
				line-height: 40px;
				text-align: center;
				text-decoration: none;
			}
			.bottom a img {
				height: 20px;
				margin-top: 10px;
			} -->
			<!-- 
			<div class="bottom">
				<a href="#"><img src="/img/topic_support_btn.png"/></a>
				<a href="#"><img src="/img/topic_reply_btn.png"/></a>
				<a href="#"><img src="/img/topic_share_btn.png"/></a>
			</div>
			-->
		</div>
	</body>
</html>
