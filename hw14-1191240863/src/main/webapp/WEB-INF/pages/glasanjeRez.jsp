<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Rezultati glasanja</title>
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
	<h1>Rezultati glasanja za ${poll.title}</h1>
	<a href="glasanje?pollID=${poll.id}">Povratak</a> na glasanje?<br>
	<a href="index.html">Povratak</a> na listu anketa?
	<h2>Tablični prikaz rezultata</h2>
	<table border="1" class="rez">
		<thead>
			<tr><th>Naslov</th><th>Broj glasova</th></tr>
		</thead>
		<tbody>
			<c:forEach var="option" items="${options}">
				<tr><td>${option.title}</td><td>${option.votesCount}</td></tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2> 
	<img alt="Pie-chart" src="glasanje-grafika?pollID=${poll.id}" />
	
	<h2>Rezultati u XLS formatu</h2>
	<a href="glasanje-xls?pollID=${poll.id}">rezultati.xls</a>

	<h2>Razno</h2>
	<h3>Pobjednici</h3>
	<ul>
		<c:forEach var="option" items="${winners}">
			<li><a href="${option.link}" target="_blank">${option.title}</a></li>
		</c:forEach>
	</ul>
</body>
</html>