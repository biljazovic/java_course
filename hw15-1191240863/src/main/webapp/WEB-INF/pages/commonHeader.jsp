<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Blog</title>
	<link rel="stylesheet" type="text/css" href="${context}/styles.css">
</head>
<body>
	<div class=header>
	<c:choose>
		<c:when test="${empty sessionScope['current.user.id']}">
			Not logged in.
		</c:when>
		<c:otherwise>
		Logged in as <b><c:out value="${sessionScope['current.user.nick']}"/></b>:<br>
			<span class=formLabel>First name:</span><c:out value="${sessionScope['current.user.fn']}"/><br>
			<span class=formLabel>Last name:</span><c:out value="${sessionScope['current.user.ln']}"/><br>
			<a href="${context}/servleti/logout">Logout</a>
		</c:otherwise>
	</c:choose>
	</div>
	<hr>