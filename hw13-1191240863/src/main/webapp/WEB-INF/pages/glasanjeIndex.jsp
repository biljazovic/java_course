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
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji vam je najdraži? Kliknite na link da biste glasali!</p>
	<ol>
		<c:forEach var="entry" items="${bands}">
			<li><a href="glasanje-glasaj?id=${entry.number}">${entry.string}</a></li>
		</c:forEach>
	</ol>
</body>
</html>