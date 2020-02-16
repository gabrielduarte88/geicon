<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<%@attribute name="field" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@attribute name="sortField" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@attribute name="order" type="java.lang.String" required="true" rtexprvalue="true" %>

<i class="fa ${sortField == field ? order == 'ASC' ? 'fa-sort-asc' : 'fa-sort-desc' : 'fa-sort'}"></i>