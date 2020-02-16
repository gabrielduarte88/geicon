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
                location.href = '${contextPath}/agentes';
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

                showModal("Deseja realmente remover o agente?", $buttons);
            });

            $('form#formAlterarAgente').validate({
                rules: {
                    'agente.nome': 'required',
                    'agente.descricao': 'required'
                },
                messages: {
                    'agente.nome': '',
                    'agente.descricao': ''
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
                    $('form#formAlterarAgente button[type=submit]').html('Alterando...').attr('disabled', true);
                    $('form#formAlterarAgente #btnRemover').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/agentes/${agente.code}/alterar',
                        data: $('form#formAlterarAgente').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formAlterarAgente button[type=submit]').html('Alterado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        $('form#formAlterarAgente button[type=submit]').html('Alterar').removeAttr('disabled');
                                        $('form#formAlterarAgente #btnRemover').removeAttr('disabled');
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

                                    $('form#formAlterarAgente button[type=submit]').html('Alterar').removeAttr('disabled');
                                    $('form#formAlterarAgente #btnRemover').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formAlterarAgente button[type=submit]').html('Alterar').removeAttr('disabled');
                                $('form#formAlterarAgente #btnRemover').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formAlterarAgente button[type=submit]').html('Alterar').removeAttr('disabled');
                            $('form#formAlterarAgente #btnRemover').removeAttr('disabled');
                        }
                    });
                }
            });
        });

        function removerRegistro() {
            $('form#formAlterarAgente #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarAgente button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/agentes/${agente.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarAgente #btnRemover').html('Removido!');

                            setTimeout(function () {
                                location.href = '${contextPath}/agentes';
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

                            $('form#formAlterarAgente #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarAgente button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarAgente #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarAgente button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarAgente #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarAgente button[type=submit]').removeAttr('disabled');
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
                Alterar agente
            </h1>
            <p>Utilize o formulário para alterar os dados do agente.</p>
            <div class="box">
                <form class="form-default" id="formAlterarAgente" onsubmit="return false">
                    <div class="form-group">
                        <label for="agente.base.code">Base de conhecimento</label>
                        <p class="form-control-static">${agente.base.titulo}</p>
                    </div>
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="agente.nome" value="${agente.nome}" maxlength="60" />
                    </div>
                    <div class="form-group">
                        <label for="descricao">Descrição <span id="descricaoMax"></span></label>
                        <textarea class="form-control" id="descricao" name="agente.descricao" maxlength="2000" maxlength-label="#descricaoMax">${agente.descricao}</textarea>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>
                    <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>