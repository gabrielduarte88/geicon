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
        list.numOfPages = ${(total / itensPorPagina) - 1};
        
        $('document').ready(function(){
            if ($('#valorFiltro').val() != '') {
                $('#indicadorFiltro').html("Buscando em '" + $('#campoFiltro option:selected').text() + "' por '" + $('#valorFiltro').val() + "'. ");
            }
        });
    </script>
</tmpl:script>

<tmpl:ajax>
    <div class="table-container">
        <div class="acoes">
            <div class="botoes">
                <button class="btn" id="btnNovo"><i class="fa fa-plus-circle"></i> novo agente</button>
            </div>
            <div class="filtro">
                <span>Buscar em</span>
                <select id="campoFiltro">
                    <option value="id" ref="integer" ${campoFiltro == 'codigo' ? 'selected' : ''}>código</option>
                    <option value="base.titulo" ref="string" ${campoFiltro == 'base.titulo' ? 'selected' : ''}>base de conhecimento</option>
                    <option value="nome" ref="string" ${campoFiltro == 'nome' ? 'selected' : ''}>nome</option>
                    <option value="data" ref="date" ${campoFiltro == 'data' ? 'selected' : ''}>data</option>
                </select>
                <span>por</span>
                <input type="text" id="valorFiltro" value="${valorFiltro}" />
                <button id="btnFiltro" data-toggle="tooltip" title="Filtrar"><i class="fa fa-search"></i></button>
                <button id="btnCancelarFiltro" class="btn-danger" data-toggle="tooltip" title="Cancelar filtro e ordenação"><i class="fa fa-times"></i></button>
            </div>
        </div>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <i class="fa fa-key" title="Campo chave" data-toggle="tooltip"></i>
                        Código
                        <a href="#" class="ordem" ref="id" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="id" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Base de conhecimento
                        <a href="#" class="ordem" ref="base.titulo" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="base.titulo" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Nome
                        <a href="#" class="ordem" ref="nome" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="nome" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Data
                        <a href="#" class="ordem" ref="data" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="data" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>&nbsp;</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="agente" items="${agentes}">
                    <tr>
                        <th scope="row">
                            <a href="#" class="visualizar" ref="${agente.code}"><fmt:formatNumber pattern="000000" value="${agente.id}" /></a>
                        </th>
                        <td>${agente.base.titulo}</td>
                        <td>${agente.nome}</td>
                        <td class="text-center"><fmt:formatDate type="both" timeStyle="short" value="${agente.data}" /></td>
                        <td class="text-center">
                            <a href="#" class="visualizar" ref="${agente.code}">
                                <button class="btn btn-primary"><i class="fa fa-edit"></i> editar</button>
                            </a>
                            <button class="btn documentos" ref="${agente.code}"><i class="fa fa-file-text"></i> documentos (${documentoService.contarDocumentosPorAgente(agente)})</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty agentes}">
                    <tr>
                        <td colspan="6">Nenhum agente encontrado</td>
                    </tr>
                </c:if>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="6">
                        <span id="indicadorFiltro"></span>
                        <c:if test="${total > 0}">
                            Exibindo ${fn:length(agentes)} de ${total} itens.
                        </c:if>
                    </td>
                </tr>
            </tfoot>
        </table>
    </div>
    <tmpl:pagination page="${pagina}" total="${total}" maxPageItems="${itensPorPagina}" />
</tmpl:ajax>