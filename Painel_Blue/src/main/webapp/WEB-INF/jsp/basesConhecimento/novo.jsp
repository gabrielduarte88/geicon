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
                location.href = '${contextPath}/bases-conhecimento';
            });

            $('form#formNovaBaseConhecimento').validate({
                rules: {
                    'baseConhecimento.titulo': 'required',
                    'baseConhecimento.proposicaoInicial': 'required',
                    'baseConhecimento.descricao': 'required'
                },
                messages: {
                    'baseConhecimento.nome': '',
                    'baseConhecimento.proposicaoInicial': '',
                    'baseConhecimento.descricao': '',
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
                    $('form#formNovaBaseConhecimento button[type=submit]').html('Incluindo...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/bases-conhecimento/novo',
                        data: $('form#formNovaBaseConhecimento').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formNovaBaseConhecimento button[type=submit]').html('Incluido!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        location.href = '${contextPath}/bases-conhecimento';
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

                                    $('form#formNovaBaseConhecimento button[type=submit]').html('Incluir').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formNovaBaseConhecimento button[type=submit]').html('Incluir').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formNovaBaseConhecimento button[type=submit]').html('Incluir').removeAttr('disabled');
                        }
                    });
                }
            });
        });
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}">
    <div class="centro">
        <div class="centro-container">
            <h1>
                Nova base de conhecimento
            </h1>
            <p>Utilize o formulário para incluir uma nova base de conhecimento.</p>
            <div class="box">
                <form class="form-default" id="formNovaBaseConhecimento" onsubmit="return false">
                    <div class="form-group">
                        <label for="titulo">Título</label>
                        <input type="text" class="form-control" id="titulo" name="baseConhecimento.titulo" maxlength="200" />
                    </div>
                    <div class="form-group">
                        <label for="proposicaoInicial">Proposição inicial <span id="proposicaoInicialMax"></span></label>
                        <textarea class="form-control" id="proposicaoInicial" name="baseConhecimento.proposicaoInicial" maxlength="500" maxlength-label="#proposicaoInicialMax"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="descricao">Descrição <span id="descricaoMax"></span></label>
                        <textarea class="form-control" id="descricao" name="baseConhecimento.descricao" maxlength="2000" maxlength-label="#descricaoMax"></textarea>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-plus-circle"></i> Incluir</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>