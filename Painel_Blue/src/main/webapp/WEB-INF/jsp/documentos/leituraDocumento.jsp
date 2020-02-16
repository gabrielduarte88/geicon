<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/js.jsp" />
    <script>
        $('document').ready(function () {
            $('#arquivo').change(function () {
                if ($(this).val() != '') {
                    $('#formDocumento').submit();
                }
            });
        });
    </script>
    <c:if test="${!empty data}">
        <script>
            var data = ${data};
            parent.$('#texto').val(data.join('\n'));
        </script>
    </c:if>
</tmpl:script>

<tmpl:body title="">
    <form method="POST" id="formDocumento" action="${contextPath}/agentes/${agente.code}/documentos/leitura-documento" enctype="multipart/form-data">
        <input type="file" id="arquivo" name="arquivo" accept=".doc,.docx,.pdf,.txt" />
    </form>
</tmpl:body>