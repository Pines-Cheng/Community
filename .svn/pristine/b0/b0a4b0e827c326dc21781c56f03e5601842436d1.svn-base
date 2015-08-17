<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>安卓手机异常</title>
</head>
<body>
	<h3 align="center">安卓手机异常汇总</h3>
	<hr />

	<table border="0" width=600px>
		<c:forEach var="fileURL" items="${fileList}" varStatus="status">
			<tr>
				<th><a href="${fileURL}">第${status.index}个异常</a></th>
				<th><a href="delete?fileURL=${fileURL}">刪除</a></th>
			</tr>
		</c:forEach>
	</table>
</body>
</html>