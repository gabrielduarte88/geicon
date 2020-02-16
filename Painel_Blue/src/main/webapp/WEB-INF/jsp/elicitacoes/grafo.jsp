<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:style>
    <c:if test="${embedded}">
        <jsp:include page="/WEB-INF/jsp/shared/css.jsp" />
    </c:if>
</tmpl:style>

<tmpl:script>
    <c:if test="${embedded}">
        <jsp:include page="/WEB-INF/jsp/shared/js.jsp" />
    </c:if>
    <script src="${contextPath}/res/components/EaselJS/lib/easeljs-0.8.2.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/components/PreloadJS/lib/preloadjs-0.6.2.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/components/TweenJS/lib/tweenjs-0.6.2.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/gpu.js" type="text/javascript"></script>

    <script src="${contextPath}/res/js/blue/blueview_core.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/blue/blueview.js" type="text/javascript"></script>
    <script>
        var blueview;

        $('document').ready(function () {
            //Inicialização
            blueview = new BlueView({
                'margin': 10,
                'events': {
                    'onDataLoaded': function () {
                        console.log('Leitura dos dados efetuadas. Foram encontrados ' + blueview.data.nodes.length
                                + ' vértices, divididos em ' + blueview.data.clusters.length
                                + ' grupos, relacionados por ' + blueview.data.links.length
                                + ' arestas.');
                    },
                    'onDataLoadError': function () {
                        exibirModal("<i class=\"fa fa-warning\"></i> <br /> Não foi possível carregar os dados do grafo.");
                    },
                    'onStart': function () {
                        $('#btnOrganizar').addClass('enabled');
                    },
                    'onStop': function () {
                        $('#btnOrganizar').removeClass('enabled');
                    }
                }
            });
            
            blueview.init($('.blueview').innerWidth(), ${embedded ? '$(\'.blueview\').parent().innerHeight()' : '$(\'div.centro\').innerHeight() - 100 - $(\'.topo\').outerHeight(true) - $(\'.centro-container h1\').outerHeight(true) - $(\'.botoes-canvas\').outerHeight(true)'});
            
            //Carregar dados
            carregarDados("${contextPath}/elicitacoes/${elicitacao.code}/dados");

            $(window).resize(function () {
                ajustarCanvas();
            });
            
            $('#btnMatriz').click(function(){
                blueview.disableCellAdjust();
                $(this).addClass('enabled');
                $('#btnMatrizElastica').removeClass('enabled');
                $('#btnGalaxyOfNodes').removeClass('enabled');
                
                $('#btnExibirRotulosNos').addClass('enabled').html('Ocultar rótulos dos conceitos');
                $('#btnExibirRotulosRels').addClass('enabled').html('Ocultar rótulos dos relações');
            });
            $('#btnMatrizElastica').click(function(){
                blueview.enableCellAdjust();
                $(this).addClass('enabled');
                $('#btnMatriz').removeClass('enabled');
                $('#btnGalaxyOfNodes').removeClass('enabled');
                
                $('#btnExibirRotulosNos').addClass('enabled').html('Ocultar rótulos dos conceitos');
                $('#btnExibirRotulosRels').addClass('enabled').html('Ocultar rótulos dos relações');
            });
            $('#btnGalaxyOfNodes').click(function(){
                blueview.enableGalaxyOfNodes();
                $(this).addClass('enabled');
                $('#btnMatriz').removeClass('enabled');
                $('#btnMatrizElastica').removeClass('enabled');
                
                $('#btnExibirRotulosNos').removeClass('enabled').html('Exibir rótulos dos conceitos');
                $('#btnExibirRotulosRels').removeClass('enabled').html('Exibir rótulos dos relações');
            });
            
            $('#btnExibirRotulosNos').click(function(){
                blueview.toggleNodeLabels();
                $(this).toggleClass('enabled');
                
                if ($(this).hasClass('enabled')) {
                    $(this).html('Ocultar rótulos dos conceitos');
                }
                else {
                    $(this).html('Exibir rótulos dos conceitos');
                }
            });
            $('#btnExibirRotulosRels').click(function(){
                blueview.toggleLinkLabels();
                $(this).toggleClass('enabled');
                
                if ($(this).hasClass('enabled')) {
                    $(this).html('Ocultar rótulos dos relações');
                }
                else {
                    $(this).html('Exibir rótulos dos relações');
                }
            });
            $('#btnAjustarCelulas').click(function(){
                blueview.toggleCellAdjust();
            });
            $('#btnOrganizar').click(function(){
                blueview.toggleRun();
                $(this).toggleClass('enabled');
            });
            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/elicitacoes';
            });
        });
        
        function ajustarCanvas() {
            blueview.redraw($('.blueview').innerWidth(), ${embedded ? '$(\'.blueview\').parent().innerHeight()' : '$(\'div.centro\').innerHeight() - 100 - $(\'.topo\').outerHeight(true) - $(\'.centro-container .buttons\').outerHeight(true) - $(\'.botoes-canvas\').outerHeight(true)'});
        }

        //Funções BlueView
        function carregarDados(src) {
            if (blueview)
                blueview.loadData(src);
        }
    </script>
</tmpl:script>

<c:if test="${embedded}">
    <tmpl:ajax>
        <div class="blueview">
            <canvas id="bluecanvas" width="0" height="0"></canvas>
        </div>

        <div class="modal animated">
            <span class="close"><i class="fa fa-times-circle"></i></span>
            <div class="modal-text"></div>
        </div>
    </tmpl:ajax>
</c:if>
<c:if test="${!embedded}">
    <tmpl:dashboard title="">
        <div class="centro">
            <div class="centro-container">
                <h1>
                    Visualizar elicitação
                </h1>
                <div class="buttons">
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                </div>
                <div class="box">
                    <div class="blueview">
                        <canvas id="bluecanvas" width="0" height="0"></canvas>
                        <div class="botoes-canvas">
                            <div>
                                <label>Modos:</label>
                                <button class="btn btn-default enabled" id="btnMatriz">Matriz estática</button>
                                <button class="btn btn-default" id="btnMatrizElastica">Matriz auto-ajustável</button>
                                <button class="btn btn-default" id="btnGalaxyOfNodes">Sem matriz ("Galáxia de Nós")</button>
                            </div>
                            <div>
                                <label>Ações:</label>
                                <button class="btn btn-default enabled" id="btnExibirRotulosNos">Ocultar rótulos dos conceitos</button>
                                <button class="btn btn-default enabled" id="btnExibirRotulosRels">Ocultar rótulos das relações</button>
                                <button class="btn btn-default enabled" id="btnOrganizar">Auto-organizar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </tmpl:dashboard>
</c:if>