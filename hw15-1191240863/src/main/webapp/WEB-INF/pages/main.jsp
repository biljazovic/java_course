<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="commonHeader.jsp" />

<c:if test="${empty sessionScope['current.user.id']}">
	<h3>Login</h3>
	<form method="post">
		<span class=formLabel>Nick:</span><input type="text" name="nick"
			value="${fn:escapeXml(form.getValue('nick'))}"><br>
		<c:if test="${form.hasError('nick')}">
			<div class=error>${form.getError('nick')}</div>
		</c:if>

		<span class=formLabel>Password:</span><input type="password"
			name="password" value="${fn:escapeXml(form.getValue('password'))}"><br>
		<c:if test="${form.hasError('password')}">
			<div class=error>${form.getError('password')}</div>
		</c:if>

		<div class="formControls">
			<input type="submit" value="Login">
		</div>
	</form>
</c:if>

<a href="register">Register a new user</a>

<h3>List of registered users:</h3>
<c:choose>
	<c:when test="${!users.isEmpty()}">
		<ul>
			<c:forEach var="user" items="${users}">
				<li><a href="author/${user.nick}">${user.nick}</a></li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		There are no registered users.
	</c:otherwise>
</c:choose>

</body>
</html>
