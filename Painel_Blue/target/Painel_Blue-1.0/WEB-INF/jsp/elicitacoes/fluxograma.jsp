<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <script src="${contextPath}/res/js/blue/elics.js" type="text/javascript"></script>
    <script>
        var elics;

        $('document').ready(function () {
            elics = new Elics('${elicitacao.code}', {
                'contextPath': '${contextPath}',
                'agent': '${elicitacao.agente.nome}',
                'domain': '${base.proposicaoInicial}',
                'editable': false
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
    <script>
        $('document').ready(function () {
            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/elicitacoes';
            });
            $('#reiniciar').click(function () {
                $('#proposicao').val('');
                $('#dividirProposicaoCampo').empty();
                $('#novaProposicao').show();
                $('#dividirProposicao').hide();
                $('#btnVoltarProp').hide();
                $('#btnVoltarDivisao').hide();
                $('#btnVoltarPergunta1').hide();
                $('#btnVoltarPergunta2').hide();
                $('#btnVoltarPergunta3').hide();
                $('#conceitoCausa, #verbo, #conceitoEfeito').val('');
                $('#pergunta1').hide();
                $('#pergunta2').hide();
                $('#pergunta3A, #pergunta3B').hide();
                $('#resultado').hide();
                $('button[selected]').removeAttr('selected');
                $('#btnSalvar').hide();
            });
            $('#btnVoltarProp').click(function () {
                $('#dividirProposicaoCampo').empty();
                $('#novaProposicao').show();
                $('#dividirProposicao').hide();
                $('#btnVoltarProp').hide();
                $('#conceitoCausa, #verbo, #conceitoEfeito').val('');
            });
            $('#apagarConceitos').click(function () {
                $('#conceitoCausa, #verbo, #conceitoEfeito').val('');
                $('span.word').removeClass('selected').removeClass('disabled');
            });
            $('#btnVoltarDivisao').click(function () {
                $('#pergunta1').hide();
                $('#dividirProposicao').show();
                $('#btnVoltarProp').show();
                $('#btnVoltarDivisao').hide();
            });
            $('#btnVoltarPergunta1').click(function () {
                $('#pergunta1').show();
                $('#pergunta2').hide();
                $('#btnFluxoA, #btnFluxoB').removeAttr('selected');
                $('#btnVoltarDivisao').show();
                $('#btnVoltarPergunta1').hide();
            });
            $('#btnVoltarPergunta2').click(function () {
                $('#pergunta2').show();
                $('#pergunta3A, #pergunta3B').hide();
                $('#btnFluxoA2, #btnFluxoB2').removeAttr('selected');
                $('#btnVoltarPergunta1').show();
                $('#btnVoltarPergunta2').hide();
            });
            $('#btnVoltarPergunta3').click(function () {
                if ($('#btnFluxoA2').is('[selected]')) {
                    $('#resultado').hide();
                    $('#pergunta3A').show();
                }
                if ($('#btnFluxoB2').is('[selected]')) {
                    $('#resultado').hide();
                    $('#pergunta3B').show();
                }
                
                $('#btnFluxoA3A, #btnFluxoB3A').removeAttr('selected');
                $('#btnFluxoA3B, #btnFluxoB3B').removeAttr('selected');
                $('#btnVoltarPergunta2').show();
                $('#btnVoltarPergunta3').hide();
                $('#btnSalvar').hide();
            });
            
            $('#adicionarProposicao').click(function() {
                var prop = $('#proposicao').val();
                
                if (prop) {
                    var palavras = prop.trim().split(' ');
                    
                    for (var i = 0; i < palavras.length; i++) {
                        var str = palavras[i];
                        
                        var span = $('<span>').addClass('word').html(str);
                        
                        $('#dividirProposicaoCampo').append(span).append(" ");
                    }
                    
                    habilitarSelecaoPalavras();
                    
                    $('.resposta').html(prop);
                    $('#novaProposicao').hide();
                    $('#dividirProposicao').show();
                    $('#btnVoltarProp').show();
                }
            });
            
            $('#iniciarQuestionario').click(function() {
                if ($('#conceitoCausa').val() != '' && $('#verbo').val() != '' && $('#conceitoEfeito').val() != '') {
                    $('.conceitoCausa').html($('#conceitoCausa').val());
                    $('.conceitoEfeito').html($('#conceitoEfeito').val());
                    $('.verbo').html($('#verbo').val());
                    $('#dividirProposicao').hide();
                    $('#btnVoltarProp').hide();
                    $('#btnVoltarDivisao').show();
                    $('#pergunta1').show();
                }
            });
            
            $('#btnFluxoA, #btnFluxoB').click(function() {
                $(this).attr('selected', true);
                $('#pergunta1').hide();
                $('#btnVoltarDivisao').hide();
                $('#pergunta2').show();
                $('#btnVoltarPergunta1').show();
            });
            
            $('#btnFluxoA2').click(function() {
                $(this).attr('selected', true);
                $('#pergunta2').hide();
                $('#btnVoltarPergunta1').hide();
                $('#pergunta3A').show();
                $('#btnVoltarPergunta2').show();
            });
            
            $('#btnFluxoB2').click(function() {
                $(this).attr('selected', true);
                $('#pergunta2').hide();
                $('#btnVoltarPergunta1').hide();
                $('#pergunta3B').show();
                $('#btnVoltarPergunta2').show();
            });
            
            $('#btnFluxoA3A, #btnFluxoA3B, #btnFluxoB3A, #btnFluxoB3B').click(function() {
                $(this).attr('selected', true);
                $('#pergunta3A, #pergunta3B').hide();
                $('#btnVoltarPergunta2').hide();
                $('#resultado').show();
                $('#btnVoltarPergunta3').show();
                $('#btnSalvar').show();
                
                resultado();
            });
            
            $('#btnSalvar').click(function() {
                var res = resultado();
                
                if (res) {
                    elics.doAddVerb($('#verbo').val(), undefined, undefined, undefined, function(proposicao) {
                        elics.doAddConcept(proposicao, 'causa', $('#conceitoCausa').val(), undefined, undefined, undefined, undefined, function(causa) {
                            elics.doAddConcept(proposicao, 'efeito', $('#conceitoEfeito').val(), undefined, undefined, undefined, undefined, function(efeito) {
                                elics.doDefineWeight(proposicao, res.v, function() {
                                    elics.doDefineConceptPositionByAgent(causa, res.cc[0], function() {
                                        elics.doDefineConceptPositionByDomain(causa, res.cc[1], function() {
                                            elics.doDefineConceptPositionByAgent(efeito, res.ce[0], function() {
                                                elics.doDefineConceptPositionByDomain(efeito, res.ce[1], function() {
                                                    $('#reiniciar').trigger('click');
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                }
            });
        });
        
        function habilitarSelecaoPalavras() {
            $('span.word').click(function() {
                if (!$(this).hasClass('disabled')) {
                    $(this).toggleClass('selected');
                }
            });
            
            $('#conceitoCausa, #verbo, #conceitoEfeito').click(function() {
                var value = '';
                
                var spans = $('span.word.selected');
                for (var i = 0; i < spans.length; i++) {
                    value += $(spans[i]).text() + ' ';
                }
                
                value = value.trim();
                
                if (value) {
                    $(this).val(value);
                }
                
                $('span.word.selected').removeClass('selected').addClass('disabled');
            });
        }
        
        function resultado() {
            var questao1 = $('#btnFluxoA').is('[selected]')
                ? true
                : $('#btnFluxoB').is('[selected]')
                    ? false
                    : undefined;
                    
            var questao2 = $('#btnFluxoA2').is('[selected]')
                ? true
                : $('#btnFluxoB2').is('[selected]')
                    ? false
                    : undefined;
                    
            var questao3A = $('#btnFluxoA3A').is('[selected]')
                ? true
                : $('#btnFluxoB3A').is('[selected]')
                    ? false
                    : undefined;
                    
            var questao3B = $('#btnFluxoA3B').is('[selected]')
                ? true
                : $('#btnFluxoB3B').is('[selected]')
                    ? false
                    : undefined;
            
            if (questao1 === undefined || questao2 === undefined || (questao3A === undefined && questao3B === undefined)) {
                $('#reiniciar').trigger('click');
                return undefined;
            }
            else {
                var res = {
                    cc: [],
                    ce: [],
                    v: 'R'
                };
                
                if (questao1) {
                    if (questao2) {
                        if (questao3A) {
                            res.cc = ['CT', 'CT'];
                            res.ce = ['CT', 'CT'];
                        }
                        else {
                            res.cc = ['PN', 'CT'];
                            res.ce = ['PN', 'PN'];
                            res.v = 'B';
                        }
                    }
                    else {
                        if (questao3A) {
                            res.cc = ['CT', 'NC'];
                            res.ce = ['CT', 'NC'];
                        }
                        else {
                            res.cc = ['NC', 'NC'];
                            res.ce = ['NC', 'PN'];
                        }
                    }
                }
                else {
                    if (questao2) {
                        if (questao3B) {
                            res.cc = ['CT', 'CT'];
                            res.ce = ['CT', 'CT'];
                        }
                        else {
                            res.cc = ['CT', 'PN'];
                            res.ce = ['PN', 'PN'];
                            res.v = '-';
                        }
                    }
                    else {
                        if (questao3B) {
                            res.cc = ['NC', 'CT'];
                            res.ce = ['NC', 'CT'];
                        }
                        else {
                            res.cc = ['NC', 'NC'];
                            res.ce = ['PN', 'NC'];
                        }
                    }
                }
                
                $('.resultadoConceitoCausa').html(
                        '<span class="valor agente ' + res.cc[0].toLowerCase() + '">' + res.cc[0] + '</span>' +
                        '<span class="valor dominio ' + res.cc[1].toLowerCase() + '">' + res.cc[1] + '</span>'
                );
                $('.resultadoConceitoEfeito').html(
                        '<span class="valor agente ' + res.ce[0].toLowerCase() + '">' + res.ce[0] + '</span>' +
                        '<span class="valor dominio ' + res.ce[1].toLowerCase() + '">' + res.ce[1] + '</span>'
                );
                $('.resultadoVerbo').html(res.v == 'R'
                    ? '<span class="reforco">+</span>'
                    : '<span class="balanceamento">-</span>');
                
                return res;
            }
        }
    </script>
</tmpl:script>

<tmpl:style>
    <style>
        span.word {
            cursor: pointer;
        }
        span.word.selected {
            background-color: #0074c7;
            color: #FFF;
        }
        span.word.disabled {
            color: #999;
        }
        button[selected] {
            background-color: #0074c7 !important;
            color: #FFF;
        }
        
        span.reforco {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 25px;
            background-color: #fc5f5f;
            text-align: center;
            color: #FFF;
            padding-top: 2px;
            margin-left: 8px;
        }
        span.balanceamento {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 25px;
            background-color: #00B8FF;
            text-align: center;
            color: #FFF;
            padding-top: 2px;
            margin-left: 8px;
        }
        span.valor.ct {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 25px;
            background-color: #27C874;
            text-align: center;
            color: #FFF;
            padding-top: 2px;
            margin-left: 8px;
        }
        span.valor.pn {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 25px;
            background-color: #EF311C;
            text-align: center;
            color: #FFF;
            padding-top: 2px;
            margin-left: 8px;
        }
        span.valor.nc {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 25px;
            background-color: #FFD800;
            text-align: center;
            color: #FFF;
            padding-top: 2px;
            margin-left: 8px;
        }
    </style>
</tmpl:style>

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
                <div class="fluxograma documentos">
                    <h2>Fluxo de elicitação  <button><i class="fa fa-arrows-alt"></i></button></h2>
                    <div class="fluxo-container" style="padding: 10px;max-height: 100%;overflow: hidden;overflow-y: auto;">
                        <div class="panel panel-default" style="padding: 10px;">
                            <form class="form-default" onsubmit="return false">
                                <div class="form-group">
                                    <label>Pergunta</label>
                                    <p class="form-control-static">${base.proposicaoInicial}</p>
                                    <div id="novaProposicao" style="float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control" id="proposicao" name="proposicao" maxlength="60"
                                                       value="Conceito causa verbo conceito efeito" />
                                                <span class="input-group-btn">
                                                  <button class="btn btn-default" id="adicionarProposicao" type="button">Ok</button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="dividirProposicao" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static" id="dividirProposicaoCampo"></p>
                                            <p class="help-block">Clicando nas palavras, e em seguida no campo de destino, identifique na sua resposta o verbo, o sujeito (causa) e o predicado (efeito)</p>
                                        </div>
                                        <div class="form-group">
                                            <label for="proposicao">Sujeito (causa)</label>
                                            <input type="text" class="form-control" id="conceitoCausa" name="conceitoCausa" maxlength="60"
                                                value="" readonly />
                                        </div>
                                        <div class="form-group">
                                            <label for="proposicao">Verbo</label>
                                            <input type="text" class="form-control" id="verbo" name="verbo" maxlength="60"
                                                value="" readonly />
                                        </div>
                                        <div class="form-group">
                                            <label for="proposicao">Predicado (efeito)</label>
                                            <input type="text" class="form-control" id="conceitoEfeito" name="conceitoEfeito" maxlength="60"
                                                value="" readonly />
                                        </div>
                                        <button type="button" class="btn btn-default" id="apagarConceitos">Limpar</button>
                                        <button type="button" class="btn btn-success" id="iniciarQuestionario">Prosseguir</button>
                                    </div>
                                    <div id="pergunta1" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static resposta"></p>
                                        </div>
                                        <div class="form-group">
                                            <span class="conceitoCausa" style="font-weight: bold;"></span> se refere ao cenário em questão?
                                        </div>
                                        <button type="button" class="btn btn-success" id="btnFluxoA">Sim</button>
                                        <button type="button" class="btn btn-success" id="btnFluxoB">Não</button>
                                    </div>
                                    <div id="pergunta2" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static resposta"></p>
                                        </div>
                                        <div class="form-group">
                                            Mudanças em <span class="conceitoCausa" style="font-weight: bold;"></span> influenciam mudanças em <span class="conceitoEfeito" style="font-weight: bold;"></span>
                                        </div>
                                        <button type="button" class="btn btn-success" id="btnFluxoA2">Sim</button>
                                        <button type="button" class="btn btn-success" id="btnFluxoB2">Não</button>
                                    </div>
                                    <div id="pergunta3A" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static resposta"></p>
                                        </div>
                                        <div class="form-group">
                                            A existência de <span class="conceitoCausa" style="font-weight: bold;"></span> reforça ou amplia <span class="conceitoEfeito" style="font-weight: bold;"></span>?
                                        </div>
                                        <button type="button" class="btn btn-success" id="btnFluxoA3A">Sim</button>
                                        <button type="button" class="btn btn-success" id="btnFluxoB3A">Não</button>
                                    </div>
                                    <div id="pergunta3B" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static resposta"></p>
                                        </div>
                                        <div class="form-group">
                                            O ambiente citado pode mudar pela ação do agente?
                                        </div>
                                        <button type="button" class="btn btn-success" id="btnFluxoA3B">Sim</button>
                                        <button type="button" class="btn btn-success" id="btnFluxoB3B">Não</button>
                                    </div>
                                    <div id="resultado" style="display: none; float: left; width: 100%; margin: 20px 0;">
                                        <div class="form-group">
                                            <label for="proposicao">Resposta</label>
                                            <p class="form-control-static resposta"></p>
                                        </div>
                                        <div class="form-group">
                                            <strong>Resultado</strong> <br />
                                            <span class="conceitoCausa"></span> <div class="resultadoConceitoCausa" style="display: inline;"></div> <br />
                                            <span class="conceitoEfeito"></span> <div class="resultadoConceitoEfeito" style="display: inline;"></div> <br />
                                            <span class="verbo"></span> <div class="resultadoVerbo" style="display: inline;"></div>
                                        </div>
                                    </div>
                                </div>
                                <button type="button" class="btn btn-default" id="btnVoltarProp" style="display: none;"><i class="fa fa-chevron-left"></i> Voltar à proposição</button>
                                <button type="button" class="btn btn-default" id="btnVoltarDivisao" style="display: none;"><i class="fa fa-chevron-left"></i> Voltar à sel. conceitos</button>
                                <button type="button" class="btn btn-default" id="btnVoltarPergunta1" style="display: none;"><i class="fa fa-chevron-left"></i> Voltar a pergunta ant.</button>
                                <button type="button" class="btn btn-default" id="btnVoltarPergunta2" style="display: none;"><i class="fa fa-chevron-left"></i> Voltar a pergunta ant.</button>
                                <button type="button" class="btn btn-default" id="btnVoltarPergunta3" style="display: none;"><i class="fa fa-chevron-left"></i> Voltar a pergunta ant.</button>
                                <button type="button" class="btn btn-sucess" id="btnSalvar" style="display: none;">Salvar</button>
                                <button type="button" class="btn btn-default" id="reiniciar">Reiniciar</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="proposicoes">
                    <h2>Proposições <button><i class="fa fa-arrows-alt"></i></button></h2>
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