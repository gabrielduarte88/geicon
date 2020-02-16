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
            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/agentes/${agente.code}/documentos';
            });

            $('#btnCarregarArquivo').click(function () {
                $('#leituradocumento').contents().find('#arquivo').click();
            });

            alterarTipoEntrada();

            $('#tipoEntrada').on('ifChanged', function (event) {
                alterarTipoEntrada();
            });

            $('.orientado textarea').change(function () {
                var texto = "";
                $('.orientado textarea').each(function () {
                    texto += $(this).val() + '\n';
                });
                $('#texto').val(texto);
                maxlengthLimit($('#texto'));
            });

            $('form#formNovoDocumento').validate({
                rules: {
                    'documento.nome': 'required',
                    'porque': 'required',
                    'oque': 'required',
                    'onde': 'required',
                    'quem': 'required',
                    'como': 'required',
                    'quando': 'required',
                    'documento.texto': 'required'
                },
                messages: {
                    'documento.nome': '',
                    'porque': '',
                    'oque': '',
                    'onde': '',
                    'quem': '',
                    'como': '',
                    'quando': '',
                    'documento.texto': ''
                },
                errorPlacement: function (error, element) {
                    //
                },
                highlight: function (element) {
                    $(element).parent().addClass("error");
                },
                unhighlight: function (element) {
                    $(element).parent().removeClass("error");
                },
                submitHandler: function (form) {
                    $('form#formNovoDocumento button[type=submit]').html('Incluindo...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/agentes/${agente.code}/documentos/novo',
                        data: $('form#formNovoDocumento').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formNovoDocumento button[type=submit]').html('Incluido!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        location.href = '${contextPath}/agentes/${agente.code}/documentos';
                                    }, 3000);
                                }
                                else {
                                    var ul = '';
                                    if (msg.extra != null) {
                                        ul = $('<ul>');

                                        for (var i in msg.extra) {
                                            ul.append($('<li>').html(msg.extra[i]));
                                        }

                                        $('div.modal .modal-text').append(ul);
                                    }

                                    showModal("<i class=\"fa fa-warning\"></i> <br />" + msg.message, ul);

                                    $('form#formNovoDocumento button[type=submit]').html('Incluir').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formNovoDocumento button[type=submit]').html('Incluir').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formNovoDocumento button[type=submit]').html('Incluir').removeAttr('disabled');
                        }
                    });
                }
            });
        });

        function alterarTipoEntrada() {
            if ($('#tipoEntrada').is(':checked')) {
                $('.orientado').show();
                $('.textual').hide();
                $('#btnCarregarArquivo').hide();
            }
            else {
                $('.orientado').hide();
                $('.textual').show();
                $('#btnCarregarArquivo').show();
            }
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}">
    <div class="centro">
        <div class="centro-container">
            <h1>
                Novo documento
            </h1>
            <p>Utilize o formulário para incluir um novo documento.</p>
            <div class="box">
                <form class="form-default" id="formNovoDocumento" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="documento.nome" maxlength="60" />
                    </div>
                    <div class="form-group icheck">
                        <label for="tipo">Tipo de entrada</label>
                    </div>
                    <div class="form-group icheck">
                        <label>
                            <input type="radio" id="tipoEntrada" name="tipoEntrada" value="1" checked /> Orientada
                        </label>
                    </div>
                    <div class="form-group">
                        <label>
                            <input type="radio" id="tipoEntrada" name="tipoEntrada" value="2" /> Textual
                        </label>
                    </div>

                    <div class="form-group orientado">
                        <label for="texto">Porquê? <span id="porqueMax"></span></label>
                        <p class="help-block">Porque é/será/foi feito? Benefícios que serão gerados ou expectativa da demanda que será atendida.</p>
                        <textarea class="form-control" id="porque" name="porque" maxlength="1000" maxlength-label="#porqueMax"></textarea>
                    </div>
                    <div class="form-group orientado">
                        <label for="texto">O que? <span id="oqueMax"></span></label>
                        <p class="help-block">O quê é/será/foi feito? Quais requisitos foram definidos pelos demandantes da resposta? Como esses requisitos auxiliam a atingir a meta?</p>
                        <textarea class="form-control" id="oque" name="oque" maxlength="1000" maxlength-label="#oqueMax"></textarea>
                    </div>
                    <div class="form-group orientado">
                        <label for="texto">Onde? <span id="ondeMax"></span></label>
                        <p class="help-block">Situe o problema/questão espacialmente.</p>
                        <textarea class="form-control" id="onde" name="onde" maxlength="1000" maxlength-label="#ondeMax"></textarea>
                    </div>
                    <div class="form-group orientado">
                        <label for="texto">Quem? <span id="quemMax"></span></label>
                        <p class="help-block">Quem faz/fez/fará? Agente que contribui para a ocorrência do problema ou a possível solução. Agente colaborador em todas as instâncias.</p>
                        <textarea class="form-control" id="quem" name="quem" maxlength="1000" maxlength-label="#quemMax"></textarea>
                    </div>
                    <div class="form-group orientado">
                        <label for="texto">Como? <span id="comoMax"></span></label>
                        <p class="help-block">Como é/foi/será feito? Meio com o qual se pretende atingir o objetivo. Meio de contornar problemas.</p>
                        <textarea class="form-control" id="como" name="como" maxlength="1000" maxlength-label="#comoMax"></textarea>
                    </div>
                    <div class="form-group orientado">
                        <label for="texto">Quando? <span id="quandoMax"></span></label>
                        <p class="help-block">Quando foi/será feito? Situe o problema/questão temporalmente.</p>
                        <textarea class="form-control" id="quando" name="quando" maxlength="1000" maxlength-label="#quandoMax"></textarea>
                    </div>

                    <div class="form-group textual">
                        <label for="texto">Texto <span id="textoMax"></span></label>
                        <textarea class="form-control lg" id="texto" name="documento.texto" maxlength="20000" maxlength-label="#textoMax"></textarea>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-plus-circle"></i> Incluir</button>
                    <button type="button" class="btn pull-right" id="btnCarregarArquivo">Carregar texto de arquivo</button>
                </form>
            </div>
            <iframe id="leituradocumento" class="hidden" src="${contextPath}/agentes/${agente.code}/documentos/leitura-documento"></iframe>
        </div>
    </div>
</tmpl:dashboard>