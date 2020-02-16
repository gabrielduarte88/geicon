<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<jsp:doBody var="body"/>

<compress:html enabled="${empty param.debug}" compressJavaScript="true" compressCss="true" simpleDoctype="true">
    ${requestScope.selfstyles}
    ${body}
    ${requestScope.selfscripts}
</compress:html>