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
                location.href = '${contextPath}/agentes/${documento.agente.code}/documentos';
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

                showModal("Deseja realmente remover o documento?", $buttons);
            });

            <c:if test="${empty bloqueado}">
                $('form#formAlterarDocumento').validate({
                    rules: {
                        'documento.nome': 'required',
                        'documento.texto': 'required'
                    },
                    messages: {
                        'documento.nome': '',
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
                        $('form#formAlterarDocumento button[type=submit]').html('Alterando...').attr('disabled', true);
                        $('form#formAlterarDocumento #btnRemover').attr('disabled', true);

                        $.ajax({
                            type: 'POST',
                            url: '${contextPath}/agentes/${documento.agente.code}/documentos/${documento.code}/alterar',
                            data: $('form#formAlterarDocumento').serialize(),
                            dataType: 'json',
                            success: function (json) {
                                if (json != null && json.msg != null) {
                                    var msg = json.msg;

                                    if (msg.type == 'success') {
                                        $('form#formAlterarDocumento button[type=submit]').html('Alterado!');
                                        showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                        setTimeout(function () {
                                            $('form#formAlterarDocumento button[type=submit]').html('Alterar').removeAttr('disabled');
                                            $('form#formAlterarDocumento #btnRemover').removeAttr('disabled');
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

                                        $('form#formAlterarDocumento button[type=submit]').html('Alterar').removeAttr('disabled');
                                        $('form#formAlterarDocumento #btnRemover').removeAttr('disabled');
                                    }
                                }
                                else {
                                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                    $('form#formAlterarDocumento button[type=submit]').html('Alterar').removeAttr('disabled');
                                    $('form#formAlterarDocumento #btnRemover').removeAttr('disabled');
                                }
                            },
                            error: function () {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                                $('form#formAlterarDocumento button[type=submit]').html('Alterar').removeAttr('disabled');
                                $('form#formAlterarDocumento #btnRemover').removeAttr('disabled');
                            }
                        });
                    }
                });
            </c:if>    
        });

        function removerRegistro() {
            $('form#formAlterarDocumento #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarDocumento button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/agentes/${documento.agente.code}/documentos/${documento.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarDocumento #btnRemover').html('Removido!');

                            setTimeout(function () {
                                location.href = '${contextPath}/agentes/${documento.agente.code}/documentos';
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

                            $('form#formAlterarDocumento #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarDocumento button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarDocumento #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarDocumento button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarDocumento #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarDocumento button[type=submit]').removeAttr('disabled');
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
                Alterar documento
            </h1>
            <p>Utilize o formulário para alterar os dados do documento.</p>
            <div class="box">
                <form class="form-default" id="formAlterarDocumento" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="documento.nome" value="${documento.nome}" maxlength="60" />
                    </div>
                    <div class="form-group">
                        <label for="texto">Texto <span id="textoMax"></span></label>
                        <textarea class="form-control lg" id="texto" name="documento.texto" maxlength="20000" maxlength-label="#textoMax">${documento.texto}</textarea>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <c:if test="${empty bloqueado}">
                        <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>
                        <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>