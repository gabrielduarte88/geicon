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

            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/usuarios';
            });

            $('#btnRemover').click(function () {
                var $buttons = $('<div>').addClass('buttons');
                $buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                        .click(function () {
                            $.unblockUI();
                        }));
                $buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-danger').html('Remover')
                        .click(function () {
                            $.unblockUI();
                            removerRegistro();
                        }));

                showModal("Deseja realmente remover o usuário?", $buttons);
            });

            $('form#formAlterarUsuario').validate({
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
                    'usuario.celular': ''
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
                    $('form#formAlterarUsuario button[type=submit]').html('Alterando...').attr('disabled', true);
                    $('form#formAlterarUsuario #btnRemover').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/usuarios/${usuario.code}/alterar',
                        data: $('form#formAlterarUsuario').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formAlterarUsuario button[type=submit]').html('Alterado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        $('form#formAlterarUsuario button[type=submit]').html('Alterar').removeAttr('disabled');
                                        $('form#formAlterarUsuario #btnRemover').removeAttr('disabled');
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

                                    $('form#formAlterarUsuario button[type=submit]').html('Alterar').removeAttr('disabled');
                                    $('form#formAlterarUsuario #btnRemover').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formAlterarUsuario button[type=submit]').html('Alterar').removeAttr('disabled');
                                $('form#formAlterarUsuario #btnRemover').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formAlterarUsuario button[type=submit]').html('Alterar').removeAttr('disabled');
                            $('form#formAlterarUsuario #btnRemover').removeAttr('disabled');
                        }
                    });
                }
            });
        });

        function removerRegistro() {
            $('form#formAlterarUsuario #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarUsuario button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/usuarios/${usuario.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarUsuario #btnRemover').html('Removido!');

                            setTimeout(function () {
                                location.href = '${contextPath}/usuarios';
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

                            $('form#formAlterarUsuario #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarUsuario button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarUsuario #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarUsuario button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarUsuario #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarUsuario button[type=submit]').removeAttr('disabled');
                }
            });
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}">
    <div class="centro">
        <div class="centro-container">
            <h1>
                Alterar usuário
            </h1>
            <p>Utilize o formulário para alterar os dados do usuário.</p>
            <div class="box">
                <form class="form-default" id="formAlterarUsuario" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="usuario.nome" value="${usuario.nome}" maxlength="60" />
                    </div>
                    <div class="form-group">
                        <label for="email">E-mail</label>
                        <input type="email" class="form-control" id="email" name="usuario.email" value="${usuario.email}" maxlength="255" />
                    </div>
                    <div class="form-group">
                        <label for="celular">Celular</label>
                        <input type="text" class="form-control" id="celular" name="usuario.celular"  value="${usuario.celular}" maxlength="20" />
                    </div>
                    <div class="form-group labelfor">
                        <label for="celular">Administrador</label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" id="administrador" name="usuario.administrador" value="1" ${usuario.administrador ? 'checked' : ''} />
                        </label>
                    </div>
                    <div class="form-group">
                        <label for="senha">Senha <button id="btnVerSenha" type="button"><i class="fa fa-eye" title="clique para visualizar a senha informada" data-toggle="tooltip"></i></button></label>
                        <input type="password" class="form-control" id="senha" name="usuario.senha" placeholder="Informe sua nova senha" maxlength="64" />
                        <input type="text" class="form-control" id="senhaView" style="display: none;" />
                        <p class="help-block">Preencha o campo somente se desejar alterar a senha.</p>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>
                    <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>