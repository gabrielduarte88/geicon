<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<%@attribute name="title" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@attribute name="description" type="java.lang.String" rtexprvalue="true" %>
<%@attribute name="open" type="java.lang.Boolean" rtexprvalue="true" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<tmpl:style>
    <jsp:include page="/WEB-INF/jsp/shared/css.jsp" />
</tmpl:style>
<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/js.jsp" />
    <script>
        var timeoutAjusteCanvas;
        
        $('document').ready(function () {
            //LOGO
            $('div.logo').click(function () {
                location.href = "${contextPath}/";
            });

            //MENU
            $('div.meusdados>a').click(function () {
                $('div.meusdados>ul').slideToggle();
                return false;
            });
            $('div.meusdados>ul').click(function (evt) {
                evt.stopPropagation();
            });
            $('body').click(function (evt) {
                $('div.meusdados>ul').slideUp();
            });

            //MENU LATERAL
            if (getScreenWidth() > 767 && '${open}' == '') {
                $('div.menu').addClass('open');
                $('div.centro').removeClass('open');
            } else {
                $('div.menu').removeClass('open');
                $('div.centro').addClass('open');
            }

            $(window).resize(function () {
                if (getScreenWidth() > 767 && '${open}' == '') {
                    $('div.menu').addClass('open');
                    $('div.centro').removeClass('open');
                } else {
                    $('div.menu').removeClass('open');
                    $('div.centro').addClass('open');
                }
            });

            $('button.btnMenu').click(function (evt) {
                $('div.menu').toggleClass('open');
                $('div.centro').toggleClass('open');
                
                if (timeoutAjusteCanvas) {
                    clearTimeout(timeoutAjusteCanvas);
                }
                timeoutAjusteCanvas = setTimeout(function() {
                    if (ajustarCanvas) {
                        ajustarCanvas();
                    }
                }, 300);
            });

            if ($('div.menu>div.menu-container>ul>li').length == 0) {
                $('div.menu').hide();
                $('button.btnMenu').hide();
                $('div.centro').addClass('full');
            }

            //LOADING BAR
            $(document).ajaxStart(function () {
                incrementLoadingBar();
            }).ajaxSuccess(function () {
                finishLoadingBar();
            }).ajaxError(function () {
                cancelLoadingBar();
            });

            //SELEÇÃO DA PESQUISA
            habilitarSelecionarPesquisa();

            $('div.loading').hide();
        });
        
        function habilitarSelecionarPesquisa() {
            $('#selecionarPesquisa').change(function () {
                $('#selecionarPesquisa').attr('disabled', true);
                $('div.meusdados>ul').slideUp();

                $('#pesquisaSelecionadaTitulo').remove();
                $('#pesquisaSelecionada').remove();

                $.ajax({
                    type: 'GET',
                    url: '${contextPath}/selecionar-pesquisa',
                    data: {'pesquisa.code': $('#selecionarPesquisa').val()},
                    dataType: 'json',
                    success: function (json) {
                        if (json != null && json.msg != null) {
                            var msg = json.msg;

                            if (msg.type == 'success') {
                                $('#selecionarPesquisa').removeAttr('disabled');

                                if (msg.message != '') {
                                    $('#instituicaoNome').after($('<li>').addClass('sep').attr('id', 'pesquisaSelecionadaTitulo').append($('<strong>').html('Pesquisa selecionada:')));
                                    $('#pesquisaSelecionadaTitulo').after($('<li>').addClass('full').attr('id', 'pesquisaSelecionada').html(msg.message));
                                }

                                reloadMenu();
                            } else {
                                showModal("<i class=\"fa fa-warning\"></i> <br />" + msg.message);
                                $('#selecionarPesquisa').removeAttr('disabled');
                            }
                        } else {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de seleção de pesquisa. <br />Por favor, tente novamente em instantes.");
                            $('#selecionarPesquisa').removeAttr('disabled');
                        }
                    },
                    error: function () {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas o serviço de seleção de pesquisa não está disponível no momento. <br />Por favor, tente novamente em instantes.");
                        $('#selecionarPesquisa').removeAttr('disabled');
                    }
                });
            });
        }
    </script>
    <c:if test="${dependentePesquisa}">
        <script>
            $('#selecionarPesquisa').change(function () {
                location.href = '${contextPath}/';
            });
        </script>
    </c:if>
</tmpl:script>

<tmpl:body title="${title}">
    <body>
        <div class="loading"></div>
        <div class="topo">
            <h2 class="title"><a href="${contextPath}/">BLUE</a> <hr /></h2>
            <button class="btnMenu" title="Exibir/Ocultar menu" data-toggle="tooltip" data-placement="bottom"><i class="fa fa-bars"></i></button>
            <div class="meusdados">
                <a href="#" id="usuarioAtual" title="${usuarioAtual.nome}" data-toggle="tooltip" data-placement="bottom">
                    <i class="fa fa-user"></i>
                    <span>${usuarioAtual.nome}</span>
                    <i class="fa fa-caret-down"></i>
                </a>
                <ul>
                    <li class="text-center" id="instituicaoNome"><strong>${instituicao.nome}</strong></li>
                    <c:if test="${!empty pesquisaAtual}">
                        <li class="sep" id="pesquisaSelecionadaTitulo"><strong>Pesquisa selecionada:</strong></li>
                        <li class="full" id="pesquisaSelecionada">${pesquisaAtual.nome}</li>
                    </c:if>
                    <li class="sep"><a href="${contextPath}/dados-pessoais"><i class="fa fa-user"></i> Dados pessoais</a></li>
                    <li><a href="${contextPath}/configuracoes"><i class="fa fa-cog"></i> Configurações</a></li>
                    <li class="sep"><a href="${contextPath}/logout"><i class="fa fa-sign-out"></i> Sair</a></li>
                </ul>
            </div>
            <hr class="loading" id="loader" />
        </div>
        <div class="principal">
            <jsp:include page="/WEB-INF/jsp/shared/menu.jsp" />
            <jsp:doBody />
        </div>
        <jsp:include page="/WEB-INF/jsp/shared/footer.jsp" />
    </body>
</tmpl:body>