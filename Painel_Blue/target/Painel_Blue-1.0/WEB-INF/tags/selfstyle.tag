<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:doBody var="selfstyle"/>

<c:choose>
    <c:when test="${empty requestScope.selfstyles}">
        <c:set var="selfstyles" scope="request">${selfstyle}</c:set>
    </c:when>    
    <c:otherwise>
        <c:set var="selfstyles" scope="request">${selfstyle}${requestScope.selfstyles}</c:set>
    </c:otherwise>
</c:choose>   