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
            $('#celular').mask('(99) 9?99999999');

            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/';
            });

            $('#btnVerSenha').mousedown(function () {
                $('#senha').hide();
                $('#senhaView').show();
            }).mouseup(function () {
                $('#senha').show();
                $('#senhaView').hide();
            }).mouseout(function () {
                $('#senha').show();
                $('#senhaView').hide();
            });

            $('#senha').change(function () {
                $('#senhaView').val($('#senha').val());
            });

            $('form#formDadosPessoais').validate({
                rules: {
                    'usuario.nome': 'required',
                    'usuario.email': {
                        'required': true,
                        'email': true
                    },
                    'usuario.celular': 'required',
                },
                messages: {
                    'usuario.nome': '',
                    'usuario.email': {
                        'required': '',
                        'email': ''
                    },
                    'usuario.celular': 'required'
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
                    $('form#formDadosPessoais button[type=submit]').html('Alterando...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/dados-pessoais',
                        data: $('form#formDadosPessoais').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formDadosPessoais button[type=submit]').html('Alterado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        $('form#formDadosPessoais button[type=submit]').html('Alterar').removeAttr('disabled');
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

                                    $('form#formDadosPessoais button[type=submit]').html('Alterar').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formDadosPessoais button[type=submit]').html('Alterar').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formDadosPessoais button[type=submit]').html('Alterar').removeAttr('disabled');
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
                <span class="fa-stack">
                    <i class="fa fa-circle fa-stack-2x"></i>
                    <i class="fa fa-user fa-stack-1x fa-inverse"></i>
                </span>
                Dados pessoais
            </h1>
            <p>Utilize o formulário para alterar os seus dados pessoais.</p>
            <div class="box">
                <form class="form-default" id="formDadosPessoais" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="usuario.nome" value="${usuarioAtual.nome}" maxlength="60" />
                    </div>
                    <div class="form-group">
                        <label for="email">E-mail</label>
                        <input type="email" class="form-control" id="email" name="usuario.email" value="${usuarioAtual.email}" maxlength="255" />
                    </div>
                    <div class="form-group">
                        <label for="celular">Celular</label>
                        <input type="text" class="form-control" id="celular" name="usuario.celular"  value="${usuarioAtual.celular}" maxlength="20" />
                    </div>
                    <hr />
                    <div class="form-group">
                        <label for="senha">Senha <button id="btnVerSenha" type="button"><i class="fa fa-eye" title="clique para visualizar a senha informada" data-toggle="tooltip"></i></button></label>
                        <input type="password" class="form-control" id="senha" name="usuario.senha" placeholder="Informe sua nova senha" maxlength="64" />
                        <input type="text" class="form-control" id="senhaView" style="display: none;" />
                        <p class="help-block">Preencha o campo somente se desejar alterar a senha.</p>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar">Cancelar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>