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
                location.href = '${contextPath}/pesquisas';
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

                showModal("Deseja realmente remover a pesquisa?", $buttons);
            });

            $('form#formAlterarPesquisa').validate({
                rules: {
                    'pesquisa.nome': 'required',
                    'pesquisa.responsavel.code': 'required'
                },
                messages: {
                    'pesquisa.nome': '',
                    'pesquisa.responsavel.code': ''
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
                    $('form#formAlterarPesquisa button[type=submit]').html('Alterando...').attr('disabled', true);
                    $('form#formAlterarPesquisa #btnRemover').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/pesquisas/${pesquisa.code}/alterar',
                        data: $('form#formAlterarPesquisa').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formAlterarPesquisa button[type=submit]').html('Alterado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    setTimeout(function () {
                                        $('form#formAlterarPesquisa button[type=submit]').html('Alterar').removeAttr('disabled');
                                        $('form#formAlterarPesquisa #btnRemover').removeAttr('disabled');
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

                                    $('form#formAlterarPesquisa button[type=submit]').html('Alterar').removeAttr('disabled');
                                    $('form#formAlterarPesquisa #btnRemover').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formAlterarPesquisa button[type=submit]').html('Alterar').removeAttr('disabled');
                                $('form#formAlterarPesquisa #btnRemover').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formAlterarPesquisa button[type=submit]').html('Alterar').removeAttr('disabled');
                            $('form#formAlterarPesquisa #btnRemover').removeAttr('disabled');
                        }
                    });
                }
            });

            exibirParticipantes();
        });

        function removerRegistro() {
            $('form#formAlterarPesquisa #btnRemover').html('Removendo...').attr('disabled', true);
            $('form#formAlterarPesquisa button[type=submit]').attr('disabled', true);

            $.ajax({
                type: 'POST',
                url: '${contextPath}/pesquisas/${pesquisa.code}/remover',
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('form#formAlterarPesquisa #btnRemover').html('Removido!');

                            setTimeout(function () {
                                location.href = '${contextPath}/pesquisas';
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

                            $('form#formAlterarPesquisa #btnRemover').html('Remover').removeAttr('disabled');
                            $('form#formAlterarPesquisa button[type=submit]').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('form#formAlterarPesquisa #btnRemover').html('Remover').removeAttr('disabled');
                        $('form#formAlterarPesquisa button[type=submit]').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('form#formAlterarPesquisa #btnRemover').html('Remover').removeAttr('disabled');
                    $('form#formAlterarPesquisa button[type=submit]').removeAttr('disabled');
                }
            });
        }

        function exibirParticipantes() {
            $.ajax({
                type: 'GET',
                url: '${contextPath}/pesquisas/${pesquisa.code}/participantes',
                success: function (data) {
                    if (data != null) {
                        $('#participantes').html(data);

                        $('#participantes input[type=checkbox],input[type=radio]').iCheck({
                            checkboxClass: 'icheckbox_square-blue',
                            radioClass: 'iradio_square-blue'
                        }).on('ifChanged', function (evt) {
                            $(evt.currentTarget).closest('tr').addClass('changed');
                        });
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                }
            });
        }

        function salvarParticipantes() {
            $('#participantes #btnSalvarParticipantes').html('Salvando...').attr('disabled', true);

            var participantes = {};

            $('#participantes tbody>tr.participante.changed').each(function () {
                var ref = $(this).attr('ref');

                var acoes = $(this).find('input:checkbox:checked').map(function () {
                    return $(this).attr('ref');
                }).get();

                participantes['participante' + ref] = '0,';
                if (acoes != '')
                    participantes['participante' + ref] += acoes.join(',');
            });

            $.ajax({
                type: 'POST',
                url: '${contextPath}/pesquisas/${pesquisa.code}/participantes',
                data: participantes,
                dataType: 'json',
                success: function (json) {
                    if (json != null && json.msg != null) {
                        var msg = json.msg;

                        if (msg.type == 'success') {
                            $('#participantes #btnSalvarParticipantes').html('Salvo!');

                            setTimeout(function () {
                                $('#participantes #btnSalvarParticipantes').html("Salvar").removeAttr('disabled');
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

                            $('#participantes #btnSalvarParticipantes').html('Salvar').removeAttr('disabled');
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                        $('#participantes #btnSalvarParticipantes').html('Salvar').removeAttr('disabled');
                    }
                },
                error: function () {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                    $('#participantes #btnSalvarParticipantes').html('Salvar').removeAttr('disabled');
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
                Alterar pesquisa
            </h1>
            <p>Utilize o formulário para alterar os dados da pesquisa.</p>
            <div class="box">
                <form class="form-default" id="formAlterarPesquisa" onsubmit="return false">
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="pesquisa.nome" value="${pesquisa.nome}" maxlength="60" />
                    </div>

                    <c:if test="${usuarioService.isAdministrador(usuarioAtual)}">
                        <div class="form-group">
                            <label for="pesquisa.responsavel.id">Responsável</label>
                            <select id="responsavel" name="pesquisa.responsavel.code">
                                <option value="">Selecione</option>
                                <c:forEach var="usuario" items="${usuarios}">
                                    <option value="${usuario.code}" ${pesquisa.responsavel.id == usuario.id ? 'selected' : ''}>${usuario.nome}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:if>
                    <c:if test="${!usuarioService.isAdministrador(usuarioAtual)}">
                        <div class="form-group">
                            <label for="pesquisa.responsavel.id">Responsável</label>
                            <p class="form-control-static">${pesquisa.responsavel.nome}</p>
                        </div>
                    </c:if>

                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-pencil"></i> Alterar</button>

                    <c:if test="${usuarioService.isAdministrador(usuarioAtual)}">
                        <button type="button" class="btn btn-danger pull-right" id="btnRemover"><i class="fa fa-times"></i> Remover</button>
                    </c:if>    
                </form>
            </div>
            <hr />
            <div class="box" id="participantes"></div>
        </div>
    </div>
</tmpl:dashboard>