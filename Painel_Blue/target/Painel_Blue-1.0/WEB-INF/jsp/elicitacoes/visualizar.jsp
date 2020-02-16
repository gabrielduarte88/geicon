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
                location.href = '${contextPath}/elicitacoes';
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

                showModal("Deseja realmente remover a elicitação?", $buttons);
            });
        });

        function removerRegistro() {
            $('form#formAlterarElicitacao #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarElicitacao button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/elicitacoes/${elicitacao.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarElicitacao #btnRemover').html('Removido!');

                            setTimeout(function () {
                                location.href = '${contextPath}/elicitacoes';
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

                            $('form#formAlterarElicitacao #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarElicitacao button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarElicitacao #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarElicitacao button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarElicitacao #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarElicitacao button[type=submit]').removeAttr('disabled');
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
                Visualizar elicitação
            </h1>
            <div class="box">
                <form class="form-default" id="formAlterarElicitacao" onsubmit="return false">
                    <div class="form-group">
                        <label for="elicitacao.base.code">Base de conhecimento</label>
                        <p class="form-control-static">${elicitacao.base.titulo}</p>
                    </div>
                    <div class="form-group">
                        <label for="nome">Agente</label>
                        <p class="form-control-static">${elicitacao.agente.nome}</p>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>