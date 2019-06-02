<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Glasanje</title>
	<style type="text/css">
		body {
			background-color: #${empty sessionScope.pickedBgCol ? "FFFFFF" : sessionScope.pickedBgCol}
		}
	</style>
</head>
<body>
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="option" items="${options}">
			<li><a href="glasanje-glasaj?votedID=${option.id}">${option.title}</a></li>
		</c:forEach>
	</ol>
	<a href="index.html">Povratak</a> na listu anketa?
</body>
</html>