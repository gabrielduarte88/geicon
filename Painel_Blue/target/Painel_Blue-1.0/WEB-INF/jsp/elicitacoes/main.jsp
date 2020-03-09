<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<fmt:setBundle basename="messages" var="lang" />

<tmpl:script>
    <jsp:include page="/WEB-INF/jsp/shared/list-js.jsp" />
    <script>
        var alvo = $('#listaElicitacoes');

        var lista = new List({
            'url': '${contextPath}/elicitacoes',
            'method': 'POST',
            'success': function (data) {
                if (data != null) {
                    alvo.html(data);
                    inicializarLista();
                    
                    if (analisando) {
                        analisarElicitacao($('.elicitacao:first'));
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de listagem. <br />Por favor, tente novamente em instantes.");
                }
            },
            'error': function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro no serviço de listagem. <br />Por favor, tente novamente em instantes.");
            }
        });

        $('document').ready(function () {
            lista.refreshList();
        });

        function inicializarLista() {
            $('#btnNovo').click(function () {
                location.href = '${contextPath}/elicitacoes/novo';
            });

            $('#btnFiltro').click(function () {
                lista.filter($('#campoFiltro').val(), $('#valorFiltro').val(), $('#campoFiltro option:selected').attr('ref'));
            });

            $('#btnCancelarFiltro').click(function () {
                lista.reset();
            });

            $('a.ordem', alvo).click(function () {
                lista.setOrder($(this).attr('ref'));
                return false;
            });
            
            $('.elicitar', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/elicitacoes/' + ref + '/elicitar';
                return false;
            });
            $('.fluxograma', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/elicitacoes/' + ref + '/fluxograma';
                return false;
            });
            $('.grafo', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/elicitacoes/' + ref + '/grafo';
                return false;
            });

            $('a.visualizar', alvo).click(function () {
                var ref = $(this).attr('ref');
                location.href = '${contextPath}/elicitacoes/' + ref;
                return false;
            });

            $('ul.pagination a').click(function () {
                if ($(this).hasClass('first')) {
                    lista.prevPage();
                }
                else if ($(this).hasClass('last')) {
                    lista.nextPage();
                }
                else {
                    lista.setPage($(this).attr('ref'));
                }

                return false;
            });

            $('[data-toggle=tooltip]', alvo).tooltip({container: 'body'});;

            inicializaFiltro();
            $('#campoFiltro').change(function () {
                $('#valorFiltro').val('');
                inicializaFiltro();
            });
        }

        function inicializaFiltro() {
            var ref = $('#campoFiltro option:selected').attr('ref');

            if (ref == 'date') {
                $('#valorFiltro').mask('99/99/9999');
            }
            else {
                $('#valorFiltro').unmask();
            }
        }
        
        var analiseCancelada;
        var analisando;
        function iniciarAnalise() {
            $('#btnAnalisar').hide();
            $('#btnCancelarAnalise').show();
            
            analiseCancelada = false;
            
            analisando = true;
            analisarElicitacao($('.elicitacao:first'));
        }
        
        function cancelarAnalise() {
            $('#btnCancelarAnalise').hide();
            $('#btnAnalisar').show();
            analiseCancelada = true;
        }
        
        function analisarElicitacao(el) {
             var ref = el.attr('ref');
                
            if (!analiseCancelada) {
                analisarContCc('${contextPath}', ref, function(res) {
                    return function(el) {
                        el.find('.analise1').html(res);
                        
                        analisarFuncPsico('${contextPath}', ref, function(res2) {
                            return function(el) {
                                el.find('.analise2').html(res2);
                                
                                $.ajax({
                                    type: 'POST',
                                    url: '${contextPath}/elicitacoes/' + ref + '/analise',
                                    data: {
                                        'elicitacao.analise1': res,
                                        'elicitacao.analise2': res2
                                    },
                                    success: function (data) {
                                        var nxt = $(el).next('.elicitacao');
                                        if (nxt) {
                                            if (nxt.attr('ref')) {
                                                analisarElicitacao(nxt);
                                            }
                                            else {
                                                if (list.page < list.numOfPages) {
                                                    list.nextPage();
                                                }
                                                else {
                                                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Análise finalizada.");
                                                    analisando = false;
                                                }
                                            }
                                        }
                                        else {
                                            showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Análise finalizada.");
                                            analisando = false;
                                        }
                                    },
                                    error: function () {
                                        showModal("<i class=\"fa fa-warning\"></i> <br /> Houve um erro ao salvar os resultados da análise.");
                                        cancelarAnalise();
                                    }
                                });
                            }(el);
                        });
                    }(el);
                });
            }
        }
    </script>
</tmpl:script>

<fmt:message bundle="${lang}" key="title" var="title" />
<tmpl:dashboard title="${title} - ${instituicao.nome}" open="true">
    <div class="centro">
        <div class="centro-container">
            <h1>Elicitações</h1>
            <c:if test="${usuarioAtual.administrador}">
                <p><a href="${contextPath}/pesquisas/${pesquisaAtual.code}">Pesquisa "${pesquisaAtual.nome}"</a> &raquo; Gerenciamento das elicitações.</p>
            </c:if>
            <c:if test="${!usuarioAtual.administrador}">
                <p>Pesquisa "${pesquisaAtual.nome}" &raquo; Gerenciamento das elicitações.</p>
            </c:if>
            <div id="listaElicitacoes" class="box"></div>
        </div>
    </div>
</tmpl:dashboard>