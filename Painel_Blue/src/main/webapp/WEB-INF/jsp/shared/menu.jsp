<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<div class="menu">
    <div class="menu-container">
        <ul>
            <c:set var="isAdministrador" value="${usuarioService.isAdministrador(usuarioAtual)}" />
            
            <li class="hoc" style="padding: 10px;">
                <strong>Selecionar pesquisa:</strong> <br />
                <select id="selecionarPesquisa">
                    <option value="">Selecione</option>
                    <c:forEach var="p" items="${usuarioPesquisas}">
                        <option value="${p.code}" ${pesquisaAtual.id == p.id ? 'selected' : ''}>${p.nome}</option>
                    </c:forEach>
                </select>
            </li>
            
            <c:if test="${isAdministrador}">
                <li><a href="${contextPath}/usuarios"><i class="fa fa-user" title="Usuários" data-toggle="tooltip" data-placement="right"></i> Usuários</a></li>
            </c:if>
            <c:if test="${isAdministrador || pesquisaService.isResponsavelPesquisas(instituicao, usuarioAtual)}">
                <li><a href="${contextPath}/pesquisas"><i class="fa fa-search" title="Pesquisas" data-toggle="tooltip" data-placement="right"></i> Pesquisas</a></li>
            </c:if>
            <c:if test="${!empty pesquisaAtual}">
                <c:if test="${isAdministrador || pesquisaService.possuiResponsabilidade(instituicao, pesquisaAtual, usuarioAtual, 'Adm. bases de conhecimento')}">
                    <li><a href="${contextPath}/bases-conhecimento"><i class="fa fa-database" title="Bases de conhecimento" data-toggle="tooltip" data-placement="right"></i> Bases de conhecimento</a></li>
                </c:if>
                <c:if test="${isAdministrador || pesquisaService.possuiResponsabilidade(instituicao, pesquisaAtual, usuarioAtual, 'Adm. agentes')}">
                    <li><a href="${contextPath}/agentes"><i class="fa fa-users" title="Agentes" data-toggle="tooltip" data-placement="right"></i> Agentes</a></li>
                </c:if>
                <c:if test="${isAdministrador || pesquisaService.possuiResponsabilidade(instituicao, pesquisaAtual, usuarioAtual, 'Elicitação')}">
                    <li><a href="${contextPath}/elicitacoes"><i class="fa fa-th" title="Elicitador" data-toggle="tooltip" data-placement="right"></i> Elicitações</a></li>
                </c:if>
            </c:if>
        </ul>
    </div>
</div>