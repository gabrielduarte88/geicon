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

                showModal("Deseja realmente remover a base de conhecimento?", $buttons);
            });

            $('form#formAlterarBaseConhecimento').validate({
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
                    $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterando...').attr('disabled', true);
                    $('form#formAlterarBaseConhecimento #btnRemover').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/bases-conhecimento/${baseConhecimento.code}/alterar',
                        data: $('form#formAlterarBaseConhecimento').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterar').removeAttr('disabled');
                                        $('form#formAlterarBaseConhecimento #btnRemover').removeAttr('disabled');
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

                                    $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterar').removeAttr('disabled');
                                    $('form#formAlterarBaseConhecimento #btnRemover').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterar').removeAttr('disabled');
                                $('form#formAlterarBaseConhecimento #btnRemover').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formAlterarBaseConhecimento button[type=submit]').html('Alterar').removeAttr('disabled');
                            $('form#formAlterarBaseConhecimento #btnRemover').removeAttr('disabled');
                        }
                    });
                }
            });
        });

        function removerRegistro() {
            $('form#formAlterarBaseConhecimento #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarBaseConhecimento button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/bases-conhecimento/${baseConhecimento.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarBaseConhecimento #btnRemover').html('Removido!');

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

                            $('form#formAlterarBaseConhecimento #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarBaseConhecimento button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarBaseConhecimento #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarBaseConhecimento button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarBaseConhecimento #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarBaseConhecimento button[type=submit]').removeAttr('disabled');
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
                Alterar base de conhecimento
            </h1>
            <p>Utilize o formulário para alterar os dados da base de conhecimento.</p>
            <div class="box">
                <form class="form-default" id="formAlterarBaseConhecimento" onsubmit="return false">
                    <div class="form-group">
                        <label for="titulo">Título</label>
                        <input type="text" class="form-control" id="titulo" name="baseConhecimento.titulo" value="${baseConhecimento.titulo}" maxlength="200" />
                    </div>
                    <div class="form-group">
                        <label for="proposicaoInicial">Proposição inicial <span id="proposicaoInicialMax"></span></label>
                        <textarea class="form-control" id="proposicaoInicial" name="baseConhecimento.proposicaoInicial" maxlength="500" maxlength-label="#proposicaoInicialMax">${baseConhecimento.proposicaoInicial}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="descricao">Descrição <span id="descricaoMax"></span></label>
                        <textarea class="form-control" id="descricao" name="baseConhecimento.descricao" maxlength="2000" maxlength-label="#descricaoMax">${baseConhecimento.descricao}</textarea>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>
                    <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>