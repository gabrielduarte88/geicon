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
            var parent = $('div.proposicoes-container');
            
            $('[data-toggle=tooltip]', parent).tooltip({container: 'body'});
            
            $('div.proposicao>span.verbo>input[type=text]', parent).change(function(){
                elics.changeVerb($(this));
            });
            
            $('div.proposicao[doc]').not('.inicial').mouseover(function () {
                elics.highlightVerb($(this));
            }).mouseout(function () {
                elics.unhighlightVerb();
            });
            
            $('div.proposicao>span.causa, div.proposicao>span.efeito', parent).click(function(){
                elics.addConcept($(this));
            });
            
            $('div.proposicao>span.questao, div.proposicao>span.reforco, div.proposicao>span.balanceamento', parent).click(function(){
                elics.defineWeight($(this));
            });
            
            $('div.proposicao>div.excluir>button', parent).click(function(){
               elics.removeProposition($(this));
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
    <c:if test="${editable == 'true'}">
        <div class="proposicao inicial">
            <p>P: ${base.proposicaoInicial}</p>
        </div>
    </c:if>
    <c:forEach var="relacao" items="${relacaoService.listarRelacoesPorElicitacao(elicitacao)}">
        <div class="proposicao" doc="${relacao.documento.code}" docId="doc${relacao.documento.id}" ref="${relacao.code}" inicial="${relacao.posicaoInicial}" final="${relacao.posicaoFinal}" titulo="${relacao.nome}">
            <span class="causa ${!empty relacao.origem ? 'active' : ''}" titulo="quem ${relacao.nome}?" data-toggle="tooltip">${!empty relacao.origem ? relacao.origem.nome : '&nbsp;'}</span>
            <span class="verbo"><input type="text" value="${relacao.nome}"  maxlength="60" /></span>
            <span class="efeito ${!empty relacao.destino ? 'active' : ''}" titulo="${relacao.nome} o que?" data-toggle="tooltip">${!empty relacao.destino ? relacao.destino.nome : '&nbsp;'}</span>
            
            <c:if test="${!empty relacao.origem && !empty relacao.destino}">
                <c:if test="${editable != 'true'}">
                    <c:if test="${relacao.peso == 'R'}"><span class="reforco" title="Quanto mais CAUSA, mais EFEITO (+)" data-toggle="tooltip">+</span></c:if>
                    <c:if test="${relacao.peso == 'B'}"><span class="balanceamento" title="Quanto mais CAUSA, menos EFEITO (-)" data-toggle="tooltip">-</span></c:if>
                </c:if>    
                <c:if test="${editable == 'true'}">
                    <c:if test="${empty relacao.peso}"><span class="questao" title="Clique para definir a influência do Conceito Causa sobre o Conceito efeito." data-toggle="tooltip">?</span></c:if>
                    <c:if test="${relacao.peso == 'R'}"><span class="reforco" title="Quanto mais CAUSA, mais EFEITO (+)" data-toggle="tooltip">+</span></c:if>
                    <c:if test="${relacao.peso == 'B'}"><span class="balanceamento" title="Quanto mais CAUSA, menos EFEITO (-)" data-toggle="tooltip">-</span></c:if>
                </c:if>    
            </c:if>
            
            <div class="aba excluir">
                <button ref="${relacao.code}"><i class="fa fa-times"></i> <span>excluir</span></button>
            </div>
        </div>
    </c:forEach>
</tmpl:ajax>