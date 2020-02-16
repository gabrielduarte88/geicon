<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:style>
    <script src="${contextPath}/res/js/boxify/modernizr.custom.js" type="text/javascript"></script>
    <jsp:include page="/WEB-INF/jsp/shared/css.jsp" />
    <link href='http://fonts.googleapis.com/css?family=Nunito:200,400,300,700' rel='stylesheet' type='text/css'>
    <link href="${contextPath}/res/css/boxify/jquery.fancybox.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/boxify/flickity.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/res/css/boxify/styles.css" rel="stylesheet">
    <link href="${contextPath}/res/css/boxify/queries.css" rel="stylesheet">
</tmpl:style> 
<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/js.jsp" />
    <script src="${contextPath}/res/js/boxify/toucheffects-min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/boxify/flickity.pkgd.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/boxify/jquery.fancybox.pack.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/boxify/waypoints.min.js" type="text/javascript"></script>
    <script src="${contextPath}/res/js/boxify/scripts.js" type="text/javascript"></script>
    <script>
        $('document').ready(function () {
            $("a[href='#']").click(function () {
                return false;
            });
            $("a.logo").click(function () {
                location.href = '${contextPath}/';
            });

            $('#btnEnviarInteresse').click(function () {
                $('form#formInteresse').submit();
            });

            $('form#formInteresse').validate({
                rules: {
                    'interesse.nome': 'required',
                    'interesse.email': {
                        'required': true,
                        'email': true
                    },
                    'interesse.organizacao': 'required'
                },
                messages: {
                    'interesse.nome': '',
                    'interesse.email': {
                        'required': '',
                        'email': ''
                    },
                    'interesse.organizacao': ''
                },
                errorPlacement: function (error, element) {
                    //
                },
                submitHandler: function (form) {
                    $('form#formInteresse #btnEnviarInteresse').html('Enviando...').attr('disabled', true);

                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/registro-interesse',
                        data: $('form#formInteresse').serialize(),
                        dataType: 'json',
                        success: function (json) {
                            if (json != null && json.msg != null) {
                                var msg = json.msg;

                                if (msg.type == 'success') {
                                    $('form#formInteresse #btnEnviarInteresse').html('Enviado!');
                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br />" + msg.message);
                                } else {
                                    var ul = '';
                                    if (msg.extra != null) {
                                        ul = $('<ul>');

                                        for (var i in msg.extra) {
                                            ul.append($('<li>').html(msg.extra[i]));
                                        }

                                        $('div.modal .modal-text').append(ul);
                                    }

                                    showModal("<i class=\"fa fa-warning\"></i> <br />" + msg.message, ul);

                                    $('form#formInteresse #btnEnviarInteresse').html('Enviar').removeAttr('disabled');
                                }
                            } else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                                $('form#formInteresse #btnEnviarInteresse').html('Enviar').removeAttr('disabled');
                            }
                        },
                        error: function () {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
                            $('form#formInteresse #btnEnviarInteresse').html('Enviar').removeAttr('disabled');
                        }
                    });
                }
            });
        });
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<compress:html enabled="${empty param.debug}" compressJavaScript="true" compressCss="true" simpleDoctype="true">
    <!DOCTYPE html>
    <html>
        <head>         
            <title>${title}</title>
            <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8; X-Content-Type-Options=nosniff;" />
            <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/favicon.ico" />
            <link rel="icon" type="/image/ico"  href="${contextPath}/favicon.ico" />
            <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
            <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
            <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
            <![endif]-->
            ${requestScope.selfstyles}
        </head>
        <body>
            <!--[if lt IE 7]>
            <p class="browsehappy">Você está utilizando um navegador <strong>desatualizado</strong>. Por favor, <a href="http://browsehappy.com/">atualize o seu navegador</a> para melhorar a sua experiência na web.</p>
            <![endif]-->
            <!-- open/close -->
            <header>
                <section class="hero">
                    <div class="texture-overlay"></div>
                    <div class="container">
                        <div class="row nav-wrapper">
                            <div class="col-md-6 col-sm-6 col-xs-6 text-left">
                                <a href="#" class="logo"><img src="${contextPath}/res/images/logo_b.png" alt="Boxify Logo"></a>
                            </div>
                            <div class="col-md-6 col-sm-6 col-xs-6 text-right navicon">
                                <a id="trigger-overlay" class="nav_slide_button nav-toggle" href="#"><span></span></a>
                            </div>
                        </div>
                        <div class="row hero-content">
                            <div class="col-md-12">
                                <h1 class="animated fadeInDown">Elicitação, armazenamento, análise e visualização de conhecimento.</h1>
                                <a href="#experimente" class="use-btn animated fadeInUp">Experimente</a>
                                <a href="#recursos" class="learn-btn animated fadeInUp">Saiba mais</a>
                            </div>
                        </div>
                    </div>
                </section>
            </header>
            <section class="video" id="recursos">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Sua organização é capaz de organizar de maneira plena o conhecimento dos colaboradores, permitindo conhecer e manipular o complexo sistema corporativo presente?</h1>
                        </div>
                    </div>
                </div>
            </section>
            <section class="features-intro">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-6 nopadding features-intro-img">
                            <div class="features-bg">
                                <!--<div class="texture-overlay"></div>-->
                                <!-- div class="features-img wp1">
                                    <img src="${contextPath}/res/images/boxify/html5-logo.png" alt="HTML5 Logo">
                                </div -->
                            </div>
                        </div>
                        <div class="col-md-6 nopadding">
                            <div class="features-slider">
                                <ul class="slides" id="featuresSlider">
                                    <li>
                                        <h1>Elicitação</h1>
                                        <p>Torne-se capaz de elicitar (obter e organizar) o conhecimento presente em documentos, manuais ou mesmo entrevistas.</p>
                                        <a href="#experimente" class="arrow-btn">Saiba mais <i class="fa fa-chevron-right"></i></a>
                                    </li>
                                    <li>
                                        <h1>Armazenamento</h1>
                                        <p>Organize de maneira estruturada o conhecimento corporativo, garantindo a capacidade de acessá-lo, efetuar pesquisas e utilizá-lo efetivamente na gestão estratégica.</p>
                                        <a href="#experimente" class="arrow-btn">Saiba mais <i class="fa fa-chevron-right"></i></a>
                                    </li>
                                    <li>
                                        <h1>Análise e visualização</h1>
                                        <p>Utilize ferramentas criadas através de avançadas pesquisas acadêmicas para analisar o conhecimento armazenado e visualizar padrões, efetuar comparações e visualizar pontos litigiosos no ambiente corporativo.</p>
                                        <a href="#experimente" class="arrow-btn">Saiba mais <i class="fa fa-chevron-right"></i></a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section class="features-list" id="caracteristicas">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">

                            <div class="col-md-4 feature-1 wp2">
                                <div class="feature-icon">
                                    <i class="fa fa-users"></i>
                                </div>
                                <div class="feature-content">
                                    <h1>Fácil utilização</h1>
                                    <p>
                                        A interface do BLUE foi contruída utilizando as melhores práticas, para que a experiência do usuário seja a melhor possível,
                                        permitindo focar-se nos dados e não no software.
                                    </p>
                                    <a href="#experimente" class="read-more-btn">Saiba mais <i class="fa fa-chevron-circle-right"></i></a>
                                </div>
                            </div>
                            <div class="col-md-4 feature-2 wp2 delay-05s">
                                <div class="feature-icon">
                                    <i class="fa fa-mobile"></i>
                                </div>
                                <div class="feature-content">
                                    <h1>Multiplataforma</h1>
                                    <p>
                                        Adaptando-se a diferentes plataformas (computadores, tablets, celulares), a utilização do BLUE
                                        é livre e desprendida de um ambiente específico.
                                    </p>
                                    <a href="#experimente" class="read-more-btn">Saiba mais <i class="fa fa-chevron-circle-right"></i></a>
                                </div>
                            </div>
                            <div class="col-md-4 feature-3 wp2 delay-1s">
                                <div class="feature-icon">
                                    <i class="fa fa-lock"></i>
                                </div>
                                <div class="feature-content">
                                    <h1>Seguro</h1>
                                    <p>
                                        Conter dados estratégicos faz com que a segurança se torne um fator primordial. O BLUE possui sistema de criptografia e 
                                        isolamento de dados para impedir acessos não autorizados.
                                    </p>
                                    <a href="#experimente" class="read-more-btn">Saiba mais <i class="fa fa-chevron-circle-right"></i></a>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </section>
            <section class="download" id="experimente">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center wp4">
                            <h1>Interessado? Deixe o seu contato</h1>
                            <p>Enviaremos as instruções de como testar nosso sistema</p>
                            <!--<a href="http://tympanus.net/codrops/?p=22554" class="download-btn">Download! <i class="fa fa-download"></i></a>-->
                            <form id="formInteresse" onsubmit="return false">
                                <input type="hidden" name="codigoInteresse" value="${codigoInteresse}" />
                                <input type="text" name="interesse.nome" placeholder="Seu nome" maxlength="60" />
                                <input type="email" name="interesse.email" placeholder="Seu e-mail" maxlength="255" />
                                <input type="text" name="interesse.organizacao" placeholder="Instituição/organização" maxlength="100" />
                                <button type="button" id="btnEnviarInteresse">Enviar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
            <footer>
                <div class="container">
                    <div class="row">
                        <div class="col-md-5">
                            <h1 class="footer-logo">
                                <img src="${contextPath}/res/images/logo_s.png" alt="Blue KMS">
                            </h1>
                            <p>Copyright 2016 - GEICon | UNICAMP</p>
                        </div>
                        <div class="col-md-7">
                            <ul class="footer-nav">
                                <li><a href="#recursos">Recursos</a></li>
                                <li><a href="#caracteristicas">Características</a></li>
                                <li><a href="#screenshots">Screenshots</a></li>
                                <li><a href="#experimente">Experimente</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </footer>
            <div class="overlay overlay-boxify">
                <nav>
                    <ul>
                        <li><a href="#recursos"><i class="fa fa-flash"></i>Recursos</a></li>
                        <li><a href="#caracteristicas"><i class="fa fa-info-circle"></i>Características</a></li>
                    </ul>
                    <ul>
                        <li><a href="#screenshots"><i class="fa fa-desktop"></i>Screenshots</a></li>
                        <li><a href="#experimente"><i class="fa fa-user"></i>Experiemente</a></li>
                    </ul>
                </nav>
            </div>
            ${requestScope.selfscripts}
            <div class="modal animated">
                <span class="close"><i class="fa fa-times-circle"></i></span>
                <div class="modal-text"></div>
            </div>
        </body>
    </html>
</compress:html>