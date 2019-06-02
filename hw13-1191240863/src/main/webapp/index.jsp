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
	<ul>
		<li><a href=colors.jsp>Background color chooser</a><br></li>
		
		<li><a href=trigonometric?b=90>SIN and COS values for integers from 0 to 90</a></li>

		<li><form action="trigonometric" method="GET">
			Start angle:<br> <input type="number" name="a" min="0" max="360" step="1" value="0"><br> 
			End angle:<br> <input type="number" name="b" min="0" max="360" step="1" value="360"><br>
			<input type="submit" value="Table">
			<input type="reset" value="Reset">
		</form></li>
		
		<li><a href=stories/funny.jsp>A little joke</a><br></li>
		
		<li><a href=powers?a=1&b=100&n=3>Powers</a><br></li>
		
		<li><a href=appinfo.jsp>Application running time</a><br></li>
		
		<li><a href=glasanje>Glasanje za omiljene bendove</a>
	</ul>
</body>
</html>