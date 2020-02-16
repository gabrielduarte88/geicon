<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <script>
        $('document').ready(function () {
            $('#btnSalvarParticipantes').click(function () {
                salvarParticipantes();
            });
        });
    </script>
</tmpl:script>

<tmpl:ajax>
    <h2>Associar usuários participantes</h2>
    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <th>
                        Nome
                    </th>
                    <c:forEach var="acao" items="${acoes}">
                        <th class="text-center">
                            ${acao.nome}
                        </th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="usuario" items="${usuarios}">
                    <c:set var="responsabilidades" value="${responsabilidadeService.mapearResponsabilidadesPorUsuario(pesquisa, usuario)}" />
                    <tr class="participante" ref="${usuario.id}">
                        <td>${usuario.nome}</td>
                        <c:forEach var="acao" items="${acoes}">
                            <td scope="row" class="text-center">
                                <input type="checkbox" ref="${acao.id}" ${responsabilidades[acao] != null ? 'checked' : ''} />   
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                <c:if test="${empty usuarios}">
                    <tr>
                        <td colspan="${fn:length(acoes) + 1}">Nenhum usuário encontrado</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <form class="form-default" onsubmit="return false">
        <button type="button" class="btn btn-success" id="btnSalvarParticipantes">Salvar</button>
    </form>
</tmpl:ajax>