<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<%@attribute name="page" type="java.lang.Integer" required="true" rtexprvalue="true" %>
<%@attribute name="total" type="java.lang.Integer" required="true" rtexprvalue="true" %>
<%@attribute name="maxPageItems" type="java.lang.Integer" required="true" rtexprvalue="true" %>

<c:set var="pages" value="${total / maxPageItems}" />
<c:set var="pages" value="${pages + (1-(pages%1))%1}" />

<c:if test="${pages > 1}">
    <nav>
        <ul class="pagination">
            <li><a href="#" class="first"><i class="fa fa-caret-left"></i></a></li>

            <c:forEach var="n" begin="0" end="${pages - 1}">
                <li class="${page == n ? 'active' : ''}"><a href="#" ref="${n}">${n + 1}</a></li>
            </c:forEach>

            <li><a href="#" class="last"><i class="fa fa-caret-right"></i></a></li>
        </ul>
    </nav>
</c:if>