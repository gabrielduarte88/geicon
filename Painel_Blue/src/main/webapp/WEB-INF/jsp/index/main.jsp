<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:style>
    <jsp:include page="/WEB-INF/jsp/shared/css.jsp" />
</tmpl:style>
<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/js.jsp" />
    <script>
        $('document').ready(function () {
            $('#celular').mask('(99) 9?99999999');

            $('#esqueciSenha').click(function () {
                $('form#login').fadeOut(function () {
                    $('form#recuperarSenha').fadeIn();
                });
                return false;
            });
            $('#esqueciSenhaVoltar').click(function () {
                $('form#recuperarSenha').fadeOut(function () {
                    $('form#login').fadeIn();
                });
                return false;
            });
            
            ajustarRodape();
            $(window).resize(function() {
                ajustarRodape();
            });

            $('form#login').validate({
                rules: {
                    'usuario.email': {
                        'required': true,
                        'email': true
                    },
                    'usuario.senha': 'required'
                },
                messages: {
                    'usuario.email': {
                        'required': '',
                        'email': ''
                    },
                    'usuario.senha': ''
                },
                errorPlacement: function (error, element) {
                    //
                },
                highlight: function (element) {
                    var fa = $(element).closest('.form-group').addClass("error").find('.fa');

                    if (!fa.hasClass('fa-times')) {
                        fa.attr('initClass', fa.attr('class'));
                        fa.attr('class', 'fa fa-times');
                    }
                },
                unhighlight: function (element) {
                    var fa = $(element).closest('.form-group').removeClass("error").find('.fa');

                    fa.attr('class', fa.attr('initClass'));
                },
                submitHandler: function (form) {
                    $('form#login button[type=submit]').html('Acessando...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/login',
                        data: $('form#login').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    location.href = '${contextPath}/dashboard';
                                } else {
                                    showModal("<i class=\"fa fa-warning\"></i> <br />" + msg.message);
                                    $('form#login button[type=submit]').html('Acessar').removeAttr('disabled');
                                }
                            } else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de autenticação. <br />Por favor, tente novamente em instantes.");
                                $('form#login button[type=submit]').html('Acessar').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas o serviço de autenticação não está disponível no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#login button[type=submit]').html('Acessar').removeAttr('disabled');
                        }
                    });
                }
            });

            $('form#recuperarSenha').validate({
                rules: {
                    'usuario.email': {
                        'required': true,
                        'email': true
                    },
                    'usuario.celular': 'required'
                },
                messages: {
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
                    var fa = $(element).closest('.form-group').addClass("error").find('.fa');

                    if (!fa.hasClass('fa-times')) {
                        fa.attr('initClass', fa.attr('class'));
                        fa.attr('class', 'fa fa-times');
                    }
                },
                unhighlight: function (element) {
                    var fa = $(element).closest('.form-group').removeClass("error").find('.fa');

                    fa.attr('class', fa.attr('initClass'));
                },
                submitHandler: function (form) {
                    $('form#recuperarSenha button[type=submit]').html('Recuperando...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/recuperar-senha',
                        data: $('form#recuperarSenha').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#recuperarSenha')[0].reset();

                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);
                                    $('form#recuperarSenha button[type=submit]').html('Recuperar').removeAttr('disabled');

                                    $('form#recuperarSenha').fadeOut(function () {
                                        $('form#login').show();
                                    });
                                } else {
                                    showModal("<i class=\"fa fa-warning\"></i> <br />" + msg.message);
                                    $('form#recuperarSenha button[type=submit]').html('Recuperar').removeAttr('disabled');
                                }
                            } else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de recuperação. <br />Por favor, tente novamente em instantes.");
                                $('form#recuperarSenha button[type=submit]').html('Recuperar').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas o serviço de recuperação não está disponível no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#recuperarSenha button[type=submit]').html('Recuperar').removeAttr('disabled');
                        }
                    });
                }
            });
        });
        
        function ajustarRodape() {
            if (getScreenHeight() < $('.main.container').outerHeight(true) + $('div.rodape').outerHeight(true)) {
                $('div.rodape').addClass('static');
            }
            else {
                $('div.rodape').removeClass('static');
            }
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:body title="${title} - ${instituicao.nome}">
    <body class="login">
        <div class="main container">
            <a href="${contextPath}/">BLUE - ${instituicao.nome}<hr/></a>
            <div class="login-container">  
                <div class="login">  
                    <form id="login" class="form-default lg" onsubmit="return false;" autocomplete="off">
                        <h2>Acessar</h2>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-at"></i></span>
                                <input type="email" class="form-control" name="usuario.email" id="email" placeholder="E-mail" maxlength="255" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                                <input type="password" class="form-control" name="usuario.senha" id="senha" placeholder="Senha" maxlength="64" />
                            </div>
                        </div>
                        <a href="#" id="esqueciSenha">Esqueci a senha <hr /></a>
                        <button type="submit" class="btn btn-success">
                            Acessar <span class="clickeffect"></span>
                        </button>
                    </form>
                    <form id="recuperarSenha" class="form-default lg" onsubmit="return false;" autocomplete="off">
                        <h2>Recuperação de senha</h2>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-at"></i></span>
                                <input type="email" class="form-control" name="usuario.email" id="email" placeholder="E-mail" maxlength="255" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-mobile"></i></span>
                                <input type="text" class="form-control" name="usuario.celular" id="celular" placeholder="Celular" maxlength="20" />
                            </div>
                        </div>
                        <a href="#" id="esqueciSenhaVoltar">Voltar <hr /></a>
                        <button type="submit" class="btn btn-success">
                            Recuperar <span class="clickeffect"></span>
                        </button>
                    </form>
                </div>
            </div>
        </div>
        <div class="rodape rodape-login">
            <div class="container">
                <p>
                    <fmt:message bundle="${lang}" key="copyright" />
                </p>
                <p class="mobile">
                    <fmt:message bundle="${lang}" key="copyright-mobile" />
                </p>
            </div>
        </div>
    </body>
</tmpl:body>