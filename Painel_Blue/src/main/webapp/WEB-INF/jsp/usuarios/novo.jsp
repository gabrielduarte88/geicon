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
                location.href = '${contextPath}/usuarios';
            });

            $('form#formNovoUsuario').validate({
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
                    $('form#formNovoUsuario button[type=submit]').html('Incluindo...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/usuarios/novo',
                        data: $('form#formNovoUsuario').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formNovoUsuario button[type=submit]').html('Incluido!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

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

                                    $('form#formNovoUsuario button[type=submit]').html('Incluir').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formNovoUsuario button[type=submit]').html('Incluir').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formNovoUsuario button[type=submit]').html('Incluir').removeAttr('disabled');
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
                Novo usuário
            </h1>
            <p>Utilize o formulário para incluir um novo usuário.</p>
            <div class="box">
                <form class="form-default" id="formNovoUsuario" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="usuario.nome" maxlength="60" />
                    </div>
                    <div class="form-group">
                        <label for="email">E-mail</label>
                        <input type="email" class="form-control" id="email" name="usuario.email" maxlength="255" />
                    </div>
                    <div class="form-group">
                        <label for="celular">Celular</label>
                        <input type="text" class="form-control" id="celular" name="usuario.celular" maxlength="20" />
                    </div>
                    <div class="form-group labelfor">
                        <label for="celular">Administrador</label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" id="administrador" name="usuario.administrador" value="1" />
                        </label>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-plus-circle"></i> Incluir</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>