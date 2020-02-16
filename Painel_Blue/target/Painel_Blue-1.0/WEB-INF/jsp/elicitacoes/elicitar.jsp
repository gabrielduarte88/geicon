<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />
<c:set var="breakline" value="<%='\n'%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <script src="${contextPath}/res/js/blue/elics.js" type="text/javascript"></script>
    <script>
        var elics;

        $('document').ready(function () {
            elics = new Elics('${elicitacao.code}', {
                'contextPath': '${contextPath}',
                'agent': '${elicitacao.agente.nome}',
                'domain': '${base.proposicaoInicial}'
            });

            $('#btnPrevisualizar').click(function () {
                var iframe = $('<iframe>').attr('src', '${contextPath}/elicitacoes/${elicitacao.code}/grafo?embedded')
                        .attr('frameborder', '0')
                        .css({
                            'width': '100%',
                            'height': getScreenHeight() * .85
                        });

                showModal(iframe, null, true);
            });
            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/elicitacoes';
            });
            $('#btnAdicionarVerbo').click(function () {
                elics.addVerb();
            });
            
            $('.documentos>h2>button, .proposicoes>h2>button, .conceitos>h2>button').click(function(){
                $(this).closest('div').toggleClass('fullscreen');
            });

            ajustarElicitador();
            $(window).resize(function () {
                ajustarElicitador();
            });

            elics.init();
        });

        function ajustarElicitador() {
            $('.box-elicitacao').css({
                'height': $('.centro-container').height()
                        - $('.centro-container>h1').outerHeight(true)
                        - $('.centro-container>p').outerHeight(true)
            });
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}" open="true">
    <div class="centro">
        <div class="centro-container">
            <h1>
                Elicitação
            </h1>
            <div class="buttons">
                <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                <button type="button" class="btn btn-primary" id="btnPrevisualizar"><i class="fa fa-search"></i> Pré-visualizar</button>
            </div>
            <p>
                ${elicitacao.base.titulo} / ${elicitacao.agente.nome}
            </p>
            <div class="box-elicitacao">
                <div class="documentos">
                    <h2>Documentos <button><i class="fa fa-arrows-alt"></i></button></h2>
                    <div class="documentos-container">
                        <c:forEach var="documento" items="${documentoService.listarDocumentosPorAgente(elicitacao.agente)}">
                            <div class="documento" ref="${documento.code}" id="doc${documento.id}">
                                <h3>${documento.nome}</h3>
                                <c:set var="cntPalavra" value="0" />
                                <c:forTokens var="paragrafo" items="${documento.texto}" delims="${breakline}">
                                    <p>
                                        <c:forTokens var="frase" items="${paragrafo}" delims=".">
                                            <c:forTokens var="sentenca" items="${frase}" delims="," varStatus="statusSentenca">
                                                <c:forTokens var="palavra" items="${sentenca}" delims=" " varStatus="statusPalavra">
                                                    <c:if test="${fn:trim(palavra) != ''}">
                                                        <span class="word" ref="${cntPalavra}" doc="${documento.code}">${palavra}</span>
                                                        ${!statusPalavra.last ? '<span>&nbsp;</span>' : ''}
                                                        <c:set var="cntPalavra" value="${cntPalavra + 1}" />
                                                    </c:if>
                                                </c:forTokens>
                                                ${fn:trim(sentenca) != '' && !statusSentenca.last ? '<span>,&nbsp;</span>' : ''}
                                                <c:set var="cntPalavra" value="${cntPalavra + 1}" />
                                            </c:forTokens>
                                            ${fn:trim(frase) != '' ? '<span>.&nbsp;</span>' : ''}
                                            <c:set var="cntPalavra" value="${cntPalavra + 1}" />
                                        </c:forTokens>
                                    </p>
                                </c:forTokens>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="proposicoes">
                    <h2>Proposições <button><i class="fa fa-arrows-alt"></i></button></h2>
                    <button id="btnAdicionarVerbo"><i class="fa fa-plus-circle"></i> adicionar verbo</button>
                    <div class="proposicoes-container"></div>
                </div>
                <div class="conceitos">
                    <h2>Conceitos <button><i class="fa fa-arrows-alt"></i></button></h2>
                    <div class="conceitos-container"></div>
                </div>
            </div>
        </div>
    </div>
</tmpl:dashboard>