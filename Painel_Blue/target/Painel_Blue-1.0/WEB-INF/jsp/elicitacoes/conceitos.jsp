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
            var parent = $('div.conceitos-container');

            $('[data-toggle=tooltip]', parent).tooltip({container: 'body'});

            $('ul.conceito>li.conceito', parent).click(function () {
                var ref = $(this).attr('id');

                $('ul.conceito, ul.correspondencia', parent).removeClass('open');
                $('ul.conceito, ul.correspondencia[objectId=' + ref + ']', parent).addClass('open');
            }).draggable({
                helper: 'clone',
                zIndex: 20
            });
            
            $('ul.correspondencia>li', parent).mouseover(function () {
                elics.highlightConcept($(this));
            }).mouseout(function () {
                elics.unhighlightConcept();
            });
            
            $('ul.conceito>li.conceito', parent).mouseover(function () {
                $('').each(function(){
                    elics.highlightConcept($(this));
                });
            }).mouseout(function () {
                elics.unhighlightConcept();
            });

            $('ul.correspondencia', parent).droppable({
                accept: "li.conceito",
                activeClass: 'readydrop',
                tolerance: "pointer",
                drop: function (event, ui) {
                    var el = ui.draggable;
                    var target = $(this);

                    elics.mergeConcepts(el, target);
                }
            });
            
            $('ul.correspondencia>li.title>input[type=text]', parent).change(function(){
                elics.changeConcept($(this));
            });
            
            $('ul.conceito>li>span.questao.agente, ul.conceito>li>span.valor.agente', parent).click(function(evt){
                elics.defineConceptPositionByAgent($(this));
                evt.stopPropagation();
            });
            
            $('ul.conceito>li>span.questao.dominio, ul.conceito>li>span.valor.dominio', parent).click(function(evt){
                elics.defineConceptPositionByDomain($(this));
                evt.stopPropagation();
            });

            $('ul.correspondencia', parent).each(function () {
                var $ul = $(this);

                $('span.dropmsg', $ul).css({
                    'top': ($ul.parent().height() / 2) - 10
                });
            });

            $('ul.correspondencia', parent).scroll(function() {
                var $ul = $(this);

                $('span.dropmsg', $ul).css({
                    'top': $ul.scrollTop() + ($ul.parent().height() / 2) - 10
                });
            });

            $('ul.correspondencia>li.title>span.excluir', parent).click(function () {
                elics.removeConcept($(this));
            });
        });
    </script>
    <c:if test="${!empty msg}">
        <script>
            showModal("<i class=\"fa fa-warning\"></i> <br /> ${msg.message}");
        </script>
    </c:if>
</tmpl:script>

<tmpl:ajax>
    <ul class="conceito">
        <c:forEach var="objeto" items="${objetoService.listarObjetosPorElicitacao(elicitacao)}">
            <li ref="${objeto.code}" class="conceito" titulo="${objeto.nome}" id="obj${objeto.id}">
                ${objeto.nome}
                
                <c:if test="${empty objeto.controlabilidadeAgente}"><span class="questao agente" title="O agente exerce controle sobre o conceito?" data-toggle="tooltip">?</span></c:if>
                <c:if test="${objeto.controlabilidadeAgente == 'CT'}"><span class="valor agente ct" title="O agente tem certeza que controla o conceito." data-toggle="tooltip">CT</span></c:if>
                <c:if test="${objeto.controlabilidadeAgente == 'PN'}"><span class="valor agente pn" title="O agente não tem certeza que controla o conceito." data-toggle="tooltip">PN</span></c:if>
                <c:if test="${objeto.controlabilidadeAgente == 'NC'}"><span class="valor agente nc" title="O agente tem certeza que não controla o conceito." data-toggle="tooltip">NC</span></c:if>
                
                <c:if test="${empty objeto.controlabilidadeDominio}"><span class="questao dominio" titulo="O conceito influencia o cenário para que o objetivo seja atingido?" data-toggle="tooltip">?</span></c:if>
                <c:if test="${objeto.controlabilidadeDominio == 'CT'}"><span class="valor dominio ct" titulo="O conceito contribui para atingir o objetivo." data-toggle="tooltip">CT</span></c:if>
                <c:if test="${objeto.controlabilidadeDominio == 'PN'}"><span class="valor dominio pn" titulo="Não se pode afirmar que o conceito contribua para atingir o objetivo." data-toggle="tooltip">PN</span></c:if>
                <c:if test="${objeto.controlabilidadeDominio == 'NC'}"><span class="valor dominio nc" titulo="O conceito não contribui para atingir o objetivo." data-toggle="tooltip">NC</span></c:if>
            </li>
        </c:forEach>
    </ul>
    <c:forEach var="objeto" items="${objetoService.listarObjetosPorElicitacao(elicitacao)}">
        <ul class="correspondencia" ref="${objeto.code}" objectId="obj${objeto.id}" titulo="${objeto.nome}">
            <li class="title"><input type="text" value="${objeto.nome}" maxlength="60" /> <span class="excluir" title="excluir" data-toggle="tooltip"><i class="fa fa-times"></i></span></li>
            <c:forEach var="ocorrencia" items="${objetoOcorrenciaService.listarOcorrenciasPorObjeto(objeto)}">
                <li doc="${ocorrencia.documento.code}" docId="doc${ocorrencia.documento.id}" inicial="${ocorrencia.posicaoInicial}" final="${ocorrencia.posicaoFinal}">
                    ${ocorrencia.valor}
                </li>
            </c:forEach>
            <span class="dropmsg">arraste o conceito aqui</span>
        </ul>
    </c:forEach>
</tmpl:ajax>