<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<style type="text/css">
		body {
			background-color: #${empty sessionScope.pickedBgCol ? "FFFFFF" : sessionScope.pickedBgCol}
		}
	</style>
</head>
<body>
	<h1>OS Usage</h1>
	<p>Here are the results of OS usage in survey we completed:</p>
	<img src="/webapp2/reportImage">
	<p>Back to <a href=/webapp2/index.jsp>index.jsp</a>?
</body>
</html>