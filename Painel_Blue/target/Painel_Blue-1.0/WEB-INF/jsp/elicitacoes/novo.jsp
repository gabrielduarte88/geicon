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
            $('#base').change(function () {
                if ($(this).val() != '') {
                    $.ajax({
                        type: 'GET',
                        url: '${contextPath}/bases-conhecimento/' + $(this).val() + '/agentes',
                        success: function (data) {
                            if (data != null) {
                                $('#agente').empty().append(data).removeAttr('disabled');
                            }
                            else {
                                $('#agente').append($('<option>').attr('value', '').html('Nenhum agente na base de conhecimento'));
                            }
                        },
                        error: function () {
                            $('#agente').empty().append($('<option>').attr('value', '').html('Selecione')).attr('disabled', true);
                        }
                    });
                }
                else {
                    $('#agente').empty().append($('<option>').attr('value', '').html('Selecione')).attr('disabled', true);
                }
            });

            $('#btnCancelar').click(function () {
                location.href = '${contextPath}/elicitacoes';
            });

            $('form#formNovaElicitacao').validate({
                rules: {
                    'elicitacao.base.code': 'required',
                    'elicitacao.agente.code': 'required'
                },
                messages: {
                    'elicitacao.base.code': '',
                    'elicitacao.agente.code': ''
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
                    $('form#formNovaElicitacao button[type=submit]').html('Incluindo...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/elicitacoes/novo',
                        data: $('form#formNovaElicitacao').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formNovaElicitacao button[type=submit]').html('Incluido!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);

                                    if (msg.extra) {
                                        setTimeout(function () {
                                            location.href = '${contextPath}/elicitacoes/' + msg.extra + '/elicitar';
                                        }, 3000);
                                    }
                                    else {
                                        setTimeout(function () {
                                            location.href = '${contextPath}/elicitacoes';
                                        }, 3000);
                                    }
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

                                    $('form#formNovaElicitacao button[type=submit]').html('Incluir').removeAttr('disabled');
                                }
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formNovaElicitacao button[type=submit]').html('Incluir').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formNovaElicitacao button[type=submit]').html('Incluir').removeAttr('disabled');
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
                Nova elicitação
            </h1>
            <p>Utilize o formulário para iniciar uma nova elicitação.</p>
            <div class="box">
                <form class="form-default" id="formNovaElicitacao" onsubmit="return false">
                    <div class="form-group">
                        <label for="elicitacao.base.code">Base de conhecimento</label>
                        <select id="base" name="elicitacao.base.code">
                            <option value="">Selecione</option>
                            <c:forEach var="base" items="${basesConhecimento}">
                                <option value="${base.code}">${base.titulo}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="elicitacao.agente.code">Agente</label>
                        <select id="agente" name="elicitacao.agente.code" disabled>
                            <option value="">Selecione</option>
                        </select>
                    </div>
                    <button type="button" class="btn btn-default" id="btnCancelar"><i class="fa fa-chevron-left"></i> Voltar</button>
                    <button type="submit" class="btn btn-success"><i class="fa fa-plus-circle"></i> Incluir</button>
                </form>
            </div>
        </div>
    </div>
</tmpl:dashboard>