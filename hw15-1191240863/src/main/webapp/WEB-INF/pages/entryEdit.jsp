<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="commonHeader.jsp" />

	<form id="entryForm" method="post">
		<div class="formControls">
			<input type="submit" value="Submit"><br> 
		</div>
		<span class=formLabel>Title:</span>
		<input type="text" name="entryTitle" value="${fn:escapeXml(form.getValue('entryTitle'))}"><br>
		<c:if test="${form.hasError('entryTitle')}">
			<div class=error>${form.getError('entryTitle')}</div>
		</c:if>
	</form>

	<textarea form="entryForm" name="entryText" rows="15" cols="70"><c:out value="${form.getValue('entryText')}"/></textarea>
	<c:if test="${form.hasError('entryText')}">
		<div class=error>${form.getError('entryText')}</div>
	</c:if>

</body>
</html>
