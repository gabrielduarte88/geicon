<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:doBody var="selfscript"/>

<c:choose>
    <c:when test="${empty requestScope.selfscripts}">
        <c:set var="selfscripts" scope="request">${selfscript}</c:set>
    </c:when>    
    <c:otherwise>
        <c:set var="selfscripts" scope="request">${selfscript}${requestScope.selfscripts}</c:set>
    </c:otherwise>
</c:choose>  