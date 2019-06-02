<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<style type="text/css">
		body {
			background-color: #${empty sessionScope.pickedBgCol ? "FFFFFF" : sessionScope.pickedBgCol};
			color: rgb(<%= (int)(Math.random()*256)%>,<%= (int)(Math.random()*256)%>,<%= (int)(Math.random()*256)%>);
			width: 50%;
		}
	</style>
</head>
<body>
	<p>A monocle walks into a bar. After a few drinks he starts to feel
		pretty good (and a little uncoordinated). He reaches for a cigarette
		but the bartender stops him. "Sorry, buddy, but due to city ordinances
		we don't allow smoking in here. You'll have to step outside to smoke."</p>

	<p>So the monocle hops off the bar stool and grabs his cigarettes
		to head outside. Meanwhile a second monocle emerges from the bathroom.
		They bump into each other as they cross paths and fall to the floor,
		hopelessly entangled. They try to get free but the more they struggle,
		the more tangled they become.</p>

	<p>The bartender looks down on this travesty and shakes his head.
		"Hey you two!" he shouts. "Stop making spectacles of yourselves!"</p>
	
	<p>Back to <a href=/webapp2/index.jsp>index.jsp</a>?
</body>
</html>