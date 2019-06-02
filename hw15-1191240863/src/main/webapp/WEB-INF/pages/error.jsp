<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="commonHeader.jsp" />
<a href="${context}/servleti/main">Back to main page</a>

<h4>${errorMessage}</h4>

</body>
</html>
