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
		table.rez td{
			text-align: center;
		}
	</style>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<h2>Tablični prikaz rezultata</h2>
	<table border="1" class="rez">
		<thead>
			<tr><th>Bend</th><th>Broj glasova</th></tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${bands}">
				<tr><td>${entry.name}</td><td>${entry.points}</td></tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2> 
	<img alt="Pie-chart" src="/webapp2/glasanje-grafika" />
	
	<h2>Rezultati u XLS formatu</h2>
	<a href="/webapp2/glasanje-xls">rezultati.xls</a>

	<h2>Razno</h2>
	<h3>Primjeri pjesama pobjedničkih bendova</h3>
	<ul>
		<c:forEach var="entry" items="${winners}">
			<li><a href="${entry.songLink}" target="_blank">${entry.name}</a></li>
		</c:forEach>
	</ul>
	<a href="/webapp2/glasanje">Povratak</a> na glasanje?
</body>
</html>