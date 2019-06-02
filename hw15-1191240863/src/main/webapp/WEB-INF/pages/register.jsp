<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />


<jsp:include page="commonHeader.jsp" />
<a href="${context}/servleti/main">Back to main page</a>

	<form method="post">
		<span class=formLabel>First Name:</span><input type="text"
			name="firstName" value="${fn:escapeXml(form.getValue('firstName'))}"><br>
		<c:if test="${form.hasError('firstName')}">
			<div class=error>${form.getError('firstName')}</div>
		</c:if>

		<span class=formLabel>Last Name:</span><input type="text"
			name="lastName" value="${fn:escapeXml(form.getValue('lastName'))}"><br>
		<c:if test="${form.hasError('lastName')}">
			<div class=error>${form.getError('lastName')}</div>
		</c:if>

		<span class=formLabel>Email:</span><input type="text" name="email"
			value="${fn:escapeXml(form.getValue('email'))}"><br>
		<c:if test="${form.hasError('email')}">
			<div class=error>${form.getError('email')}</div>
		</c:if>

		<span class=formLabel>Nick:</span><input type="text" name="nick"
			value="${fn:escapeXml(form.getValue('nick'))}"><br>
		<c:if test="${form.hasError('nick')}">
			<div class=error>${form.getError('nick')}</div>
		</c:if>

		<span class=formLabel>Password</span><input type="password"
			name="password" value="${fn:escapeXml(form.getValue('password'))}"><br>
		<c:if test="${form.hasError('password')}">
			<div class=error>${form.getError('password')}</div>
		</c:if>


		<div class="formControls">
			<input type="submit" value="Register">
		</div>
	</form>
</body>
</html>
