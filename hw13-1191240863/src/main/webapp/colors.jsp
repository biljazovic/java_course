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
	<p>Choose color for the background</p>
	<ul>
		<li><a href="/webapp2/setcolor?bgcolor=FFFFFF">WHITE</a><br></li>
		<li><a href="/webapp2/setcolor?bgcolor=FF0000">RED</a><br></li>
		<li><a href="/webapp2/setcolor?bgcolor=008000">GREEN</a><br></li>
		<li><a href="/webapp2/setcolor?bgcolor=00FFFF">CYAN</a><br></li>
	</ul>
	<p>Back to <a href=/webapp2/index.jsp>index.jsp</a>?
</body>
</html>