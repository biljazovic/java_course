<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="commonHeader.jsp" />
<a href="${context}/servleti/main">Back to main page</a>

	<c:choose>
		<c:when test="${!entries.isEmpty()}">
			<h3>List of blog entries from ${nick}</h3>
			<ul>
				<c:forEach var="entry" items="${entries}">
					<li><a href="${nick}/${entry.id}"><c:out value="${entry.title}"/></a></li>
				</c:forEach>
			</ul>
		</c:when>
		<c:otherwise>
			<h4>This user has no blog entries.</h4>
		</c:otherwise>
	</c:choose>

	<c:if test="${nick == sessionScope['current.user.nick']}">
		<a href="${nick}/new">Add a new blog entry</a>
	</c:if>
</body>
</html>
