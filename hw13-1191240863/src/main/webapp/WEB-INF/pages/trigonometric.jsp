<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<p>Requested results:</p>
	<table border="1">
		<thead>
			<tr><th>Number</th><th>sin</th><th>cos</th>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${trigValues}">
				<tr><td>${entry.number}</td><td>${entry.sin}</td><td>${entry.cos}</td></tr>
			</c:forEach>
		</tbody>
	</table>
	<p>Back to <a href=/webapp2/index.jsp>index.jsp</a>?
</body>
</html>