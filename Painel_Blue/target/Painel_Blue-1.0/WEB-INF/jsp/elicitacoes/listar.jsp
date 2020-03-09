<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <script src="${contextPath}/res/js/blue/analise.js" type="text/javascript"></script>
    <script>
        list.numOfPages = ${(total / itensPorPagina) - 1};
        
        $('document').ready(function(){
            if ($('#valorFiltro').val() != '') {
                $('#indicadorFiltro').html("Buscando em '" + $('#campoFiltro option:selected').text() + "' por '" + $('#valorFiltro').val() + "'. ");
            }
            
            $('#btnAnalisar').click(function () {
                var $buttons = $('<div>').addClass('buttons');
                $buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                        .click(function () {
                            $.unblockUI();
                        }));
                $buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-info').html('Analisar')
                        .click(function () {
                            $.unblockUI();
                            iniciarAnalise();
                        }));

                showModal("Deseja realmente iniciar a análise? O processo pode demorar alguns minutos.", $buttons);
            });
            
            $('#btnCancelarAnalise').click(function () {
                cancelarAnalise();
            });
        });
    </script>
</tmpl:script>

<tmpl:ajax>
    <div class="table-container">
        <div class="acoes">
            <div class="botoes">
                <button class="btn" id="btnNovo"><i class="fa fa-plus-circle"></i> nova elicitação</button>
                <button class="btn" id="btnAnalisar" style="background-color: #090;"><i class="fa fa-eye"></i> analisar elicitações</button>
                <button class="btn" id="btnCancelarAnalise" style="background-color: #900; display: none;"><i class="fa fa-times"></i> cancelar analise</button>
            </div>
            <div class="filtro">
                <span>Buscar em</span>
                <select id="campoFiltro">
                    <option value="id" ref="integer" ${campoFiltro == 'codigo' ? 'selected' : ''}>código</option>
                    <option value="base.titulo" ref="string" ${campoFiltro == 'base.titulo' ? 'selected' : ''}>base de conhecimento</option>
                    <option value="agente.nome" ref="string" ${campoFiltro == 'agente.nome' ? 'selected' : ''}>agente</option>
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
                        Agente
                        <a href="#" class="ordem" ref="agente.nome" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="agente.nome" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Score H
                        <a href="#" class="ordem" ref="score" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="score" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Score L
                        <a href="#" class="ordem" ref="score2" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="score2" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Cont. Cc
                        <a href="#" class="ordem" ref="analise1" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="score" sortField="${campoOrdem}" order="${ordem}" />
                        </a>
                    </th>
                    <th>
                        Func. Psico
                        <a href="#" class="ordem" ref="analise2" title="Ordenar" data-toggle="tooltip">
                            <tmpl:sort-icon field="analise2" sortField="${campoOrdem}" order="${ordem}" />
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
                <c:forEach var="elicitacao" items="${elicitacoes}">
                    <tr class="elicitacao" ref="${elicitacao.code}">
                        <th scope="row">
                            <a href="#" class="visualizar" ref="${elicitacao.code}"><fmt:formatNumber pattern="000000" value="${elicitacao.id}" /></a>
                        </th>
                        <td>${elicitacao.base.titulo}</td>
                        <td>${elicitacao.agente.nome}</td>
                        <td>${elicitacao.score}</td>
                        <td>${elicitacao.score2}</td>
                        <td class="analise1">${elicitacao.analise1}</td>
                        <td class="analise2">${elicitacao.analise2}</td>
                        <td class="text-center"><fmt:formatDate type="both" timeStyle="short" value="${elicitacao.data}" /></td>
                        <td class="text-center button-container">
                            <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                    Opções
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                    <li><a href="#" class="visualizar" ref="${elicitacao.code}">Editar</a></li>
                                    <li><a href="#" class="elicitar" ref="${elicitacao.code}">Elicitar</a></li>
                                    <li><a href="#" class="fluxograma" ref="${elicitacao.code}">Fluxo elicitação</a></li>
                                    <li><a href="#" class="grafo" ref="${elicitacao.code}">Visualizar</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty elicitacoes}">
                    <tr>
                        <td colspan="6">Nenhuma elicitação encontrada</td>
                    </tr>
                </c:if>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="7">
                        <span id="indicadorFiltro"></span>
                        <c:if test="${total > 0}">
                            Exibindo ${fn:length(elicitacoes)} de ${total} itens.
                        </c:if>
                    </td>
                </tr>
            </tfoot>
        </table>
    </div>
    <tmpl:pagination page="${pagina}" total="${total}" maxPageItems="${itensPorPagina}" />
</tmpl:ajax>