<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="title" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@attribute name="description" type="java.lang.String" rtexprvalue="true" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<jsp:doBody var="body"/>

<compress:html enabled="${empty param.debug}" compressJavaScript="true" compressCss="true" simpleDoctype="true">
    <!DOCTYPE html>
    <html>
        <head>         
            <title>${title}</title>

            <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8; X-Content-Type-Options=nosniff;" />

            <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/favicon.ico" />
            <link rel="icon" type="/image/ico"  href="${contextPath}/favicon.ico" />

            ${requestScope.selfheads}
            ${requestScope.selfstyles}
        </head>

        ${body}

        <div class="modal animated">
            <span class="close"><i class="fa fa-times-circle"></i></span>
            <div class="modal-text"></div>
        </div>

        ${requestScope.selfscripts}
    </html>
</compress:html>