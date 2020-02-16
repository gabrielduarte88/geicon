<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:doBody var="selfhead"/>

<c:choose>
    <c:when test="${empty requestScope.selfheads}">
        <c:set var="selfheads" scope="request">${selfhead}</c:set>
    </c:when>    
    <c:otherwise>
        <c:set var="selfheads" scope="request">${selfhead}${requestScope.selfheads}</c:set>
    </c:otherwise>
</c:choose>   