<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" /> 

<c:set var="contextPath" value="${environment.get('contextPath')}" />

<tmpl:head>
    <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/favicon.ico" />
    <link rel="icon" type="/image/ico"  href="${contextPath}/favicon.ico" />
</tmpl:head>
<tmpl:style>
    <link href="${contextPath}/res/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/bootstrap-theme.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/animate.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/theme.css" rel="stylesheet" type="text/css"/>
</tmpl:style>
<tmpl:script>
    <script src="${contextPath}/res/js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/jquery.blockui.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/tools.js" type="text/javascript"></script>
</tmpl:script>

<c:set var="erro">erro.${param.err}</c:set>
<c:set var="erro_desc">erro.${param.err}.desc</c:set>

<fmt:message bundle="${lang}" key="${erro_desc}" var="errdesc" />

<tmpl:body title="Erro ${param.err} - Blue">
    <div class="erro animated bounceInDown">
        <h1>Lamentamos, mas...</h1>
        <p>${errdesc}</p>
        <p>
            <a href="javascript:history.back()" title="Voltar"><i class="fa fa-chevron-left"></i> voltar</a> |
            <a href="${contextPath}/" title="Página inicial"><i class="fa fa-home"></i> página principal</a>
        </p>
    </div>
    <div class="rodape rodape-login">
        <div class="container">
            <p>
                <fmt:message bundle="${lang}" key="copyright" />
            </p>
            <p class="mobile">
                <fmt:message bundle="${lang}" key="copyright-mobile" />
            </p>
        </div>
    </div>
</tmpl:body>