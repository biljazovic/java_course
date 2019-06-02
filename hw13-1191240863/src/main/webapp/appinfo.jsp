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
	<% Long time = System.currentTimeMillis() - (Long)(request.getServletContext().getAttribute("startTime"));
		Long days = time / (1000 * 60 * 60 * 24);
		Long hours = time / (1000 * 60 * 60) % 24;
		Long minutes = time / (1000 * 60) % 60;
		Long seconds = time / (1000) % 60;
		Long miliseconds = time % 1000;
	%> 
	This application is running for <b><%= days==0 ? "" : days+" days"%>
	<%= hours==0 ? "" : hours+" hours" %> <%= minutes==0 ? "" : minutes+" minutes"%> 
	<%= seconds==0 ? "" : seconds+" seconds and"%> <%= miliseconds+" miliseconds"%></b>.<br>

	<p>Back to <a href=/webapp2/index.jsp>index.jsp</a>?
	
</body>
</html>