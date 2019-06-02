<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<jsp:include page="commonHeader.jsp" />

<c:if test="${nick == sessionScope['current.user.nick']}">
	<a href="edit?entryid=${entry.id}">Edit this blog entry.</a><br>
</c:if>
<a href="${context}/servleti/main">Back to main page</a>
<hr>

<h1>
	<c:out value="${entry.title}" />
</h1>

<small style="color: gray">created at ${entry.createdAt}, last
	modified at ${entry.lastModifiedAt}, by <c:out
		value="${entry.blogUser.nick}" />
</small>

<div style="white-space: pre-wrap;"><p><c:out value="${entry.text}"/></p></div><hr>

<c:choose>
	<c:when test="${!entry.comments.isEmpty()}">
		<h3>Comments:</h3>
		<ul>
			<c:forEach var="comment" items="${entry.comments}">
				<li>
					<i>User with email <c:out value="${comment.userEmail}" /> commented at ${comment.postedOn}: </i><br>
						<div style="padding: left=10px; border: 1px solid black">
							<c:out value="${comment.message}" />
						</div>
				</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
			This blog entry has no comments.
	</c:otherwise>
</c:choose>

<form id="commentForm" method="post">
	<input type="submit" value="Comment">
	<c:if test="${empty sessionScope['current.user.id']}">
		<br>
		<span class=formLabel>Email:</span>
		<input type="text" value="${fn:escapeXml(form.getValue('email'))}" name="email">
		<c:if test="${form.hasError('email')}">
			<div class=error>${form.getError('email')}</div>
		</c:if>
	</c:if>
</form>

<textarea form="commentForm" name="comment" rows="5" cols="50"><c:out value="${form.getValue('comment')}" /></textarea>
<c:if test="${form.hasError('comment')}">
	<div class=error>${form.getError('comment')}</div>
</c:if>

</body>
</html>
