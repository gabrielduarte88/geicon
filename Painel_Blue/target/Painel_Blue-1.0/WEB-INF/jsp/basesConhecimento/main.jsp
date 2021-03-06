<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/list-js.jsp" />
    <script>
        var alvo = $('#listaBases');

        var lista = new List({
            'url': '${contextPath}/bases-conhecimento',
            'method': 'POST',
            'success': function (data) {
                if (data != null) {
                    $('[data-toggle=tooltip]', alvo).tooltip('destroy');
                    alvo.html(data);
                    inicializarLista();
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de listagem. <br />Por favor, tente novamente em instantes.");
                }
            },
            'error': function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de listagem. <br />Por favor, tente novamente em instantes.");
            }
        });

        $('document').ready(function () {
            lista.refreshList();
        });

        function inicializarLista() {
            $('#btnNovo').click(function () {
                location.href = '${contextPath}/bases-conhecimento/novo';
            });

            $('#btnFiltro').click(function () {
                lista.filter($('#campoFiltro').val(), $('#valorFiltro').val(), $('#campoFiltro option:selected').attr('ref'));
            });

            $('#btnCancelarFiltro').click(function () {
                lista.reset();
            });

            $('a.ordem', alvo).click(function () {
                lista.setOrder($(this).attr('ref'));
                return false;
            });

            $('a.visualizar', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/bases-conhecimento/' + ref;
                return false;
            });
            
            $('button.agentes', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/base-conhecimento/' + ref + '/agente';
                return false;
            });
            
            $('button.elicitacoes', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/base-conhecimento/' + ref + '/elicitacao';
                return false;
            });

            $('ul.pagination a').click(function () {
                if ($(this).hasClass('first')) {
                    lista.prevPage();
                }
                else if ($(this).hasClass('last')) {
                    lista.nextPage();
                }
                else {
                    lista.setPage($(this).attr('ref'));
                }

                return false;
            });

            $('[data-toggle=tooltip]', alvo).tooltip({container: 'body'});;

            inicializaFiltro();
            $('#campoFiltro').change(function () {
                $('#valorFiltro').val('');
                inicializaFiltro();
            });
        }

        function inicializaFiltro() {
            var ref = $('#campoFiltro option:selected').attr('ref');

            if (ref == 'date') {
                $('#valorFiltro').mask('99/99/9999');
            }
            else {
                $('#valorFiltro').unmask();
            }
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}">
    <div class="centro">
        <div class="centro-container">
            <h1>Bases de conhecimento</h1>
            <c:if test="${usuarioAtual.administrador}">
                <p><a href="${contextPath}/pesquisas/${pesquisaAtual.code}">Pesquisa "${pesquisaAtual.nome}"</a> &raquo; Gerenciamento das bases de conhecimento.</p>
            </c:if>
            <c:if test="${!usuarioAtual.administrador}">
                <p>Pesquisa "${pesquisaAtual.nome}" &raquo; Gerenciamento das bases de conhecimento.</p>
            </c:if>
            <div id="listaBases" class="box"></div>
        </div>
    </div>
</tmpl:dashboard>