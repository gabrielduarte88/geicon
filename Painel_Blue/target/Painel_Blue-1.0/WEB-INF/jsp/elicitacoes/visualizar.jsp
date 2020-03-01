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

            $('#btnAnaliseContCc').click(function () {
                var url = '${contextPath}/elicitacoes/${elicitacao.code}/dados';
                
                carregarDados(url, function(json) {
                    if (json && json.nodes) {
                        var total = 0;

                        //contar conexões
                        for (var node of json.nodes) {
                            var conexoes = 0;

                            for (var link of json.links) {
//                                if (link.source == node.id || link.target == node.id) {
                                if (link.source == node.id) {
                                    conexoes++;
                                }
                                total++;
                            }

                            node.connections = conexoes;
                        }

                        //calcular média e desv. pad.
                        var lista = json.nodes.map(function(node) {
                            return node.connections;
                        });

                        var m = media(lista);
                        var d = desvpad(lista);

                        //determinar hubs
                        var qtdeHubs = 0;

                        for (var node of json.nodes) {
                            node.hub = node.connections >= m + 2 * d;
                            
                            if (node.hub) {
                                qtdeHubs++;
                            }
                        }
                        
                        if (qtdeHubs > 0) {
                            json.nodes = json.nodes.sort(function(node, node2) {
                                return node2.connections - node.connections;
                            });

                            //classificar
                            var ativos = 0;
                            var reflexivos = 0;
                            var teoricos = 0;
                            var pragmaticos = 0;

                            for (var node of json.nodes) {
                                if (node.hub) {
                                    var teoricoPragmatico;
                                    var ativoReflexivo;

                                    var group = json.clusters.find(function(g) {
                                        return g.id == node.group;
                                    });

                                    switch (group.X) {
                                        case 0: {
                                            teoricoPragmatico = 'Teórico';
                                            teoricos++;
                                            break;
                                        }
                                        case 1: {
                                            teoricoPragmatico = 'Teórico-Pragmático';
                                            teoricos++;
                                            pragmaticos++;
                                            break;
                                        }
                                        case 2: {
                                            teoricoPragmatico = 'Pragmático';
                                            pragmaticos++;
                                            break;
                                        }
                                    }

                                    switch (group.Y) {
                                        case 0: {
                                            ativoReflexivo = 'Ativo';
                                            ativos++;
                                            break;
                                        }
                                        case 1: {
                                            ativoReflexivo = 'Ativo-Reflexivo';
                                            ativos++;
                                            reflexivos++;
                                            break;
                                        }
                                        case 2: {
                                            ativoReflexivo = 'Reflexivo';
                                            reflexivos++;
                                            break;
                                        }
                                    }

                                    node.teoricoPragmatico = teoricoPragmatico;
                                    node.ativoReflexivo = ativoReflexivo;
                                }
                            }

                            ativos = ativos / qtdeHubs;
                            reflexivos = reflexivos / qtdeHubs;
                            teoricos = teoricos / qtdeHubs;
                            pragmaticos = pragmaticos / qtdeHubs;

                            if (qtdeHubs > 1) {
                                if (teoricoPragmatico != 'Teórico-Pragmático') {
                                    if (ativoReflexivo == 'Teórico') {
                                        if (pragmaticos > .5) {
                                            ativoReflexivo = 'Teórico-Pragmático';
                                        }
                                    }

                                    if (ativoReflexivo == 'Pragmático') {
                                        if (teoricos > .5) {
                                            ativoReflexivo = 'Teórico-Pragmático';
                                        }
                                    }
                                }

                                if (ativoReflexivo != 'Ativo-Reflexivo') {
                                    if (ativoReflexivo == 'Ativo') {
                                        if (reflexivos > .5) {
                                            ativoReflexivo = 'Ativo-Reflexivo';
                                        }
                                    }

                                    if (ativoReflexivo == 'Reflexivo') {
                                        if (ativos > .5) {
                                            ativoReflexivo = 'Ativo-Reflexivo';
                                        }
                                    }
                                }
                            }

                            showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Classificação do agente: " + teoricoPragmatico + " / " + ativoReflexivo + ".");
                        }
                        else {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Nenhum hub foi encontrado.");
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Nenhum conceito foi adicionado ao MCE.");
                    }
                });
            });
            
            $('#btnFuncPsico').click(function () {
                var url = '${contextPath}/elicitacoes/${elicitacao.code}/dados';
                
                carregarDados(url, function(json) {
                    if (json && json.nodes) {
                        var total = 0;

                        //contar conexões
                        for (var node of json.nodes) {
                            var conexoes = 0;

                            for (var link of json.links) {
//                                if (link.source == node.id || link.target == node.id) {
                                if (link.source == node.id) {
                                    conexoes++;
                                }
                                total++;
                            }

                            node.connections = conexoes;
                        }

                        //calcular média e desv. pad.
                        var lista = json.nodes.map(function(node) {
                            return node.connections;
                        });

                        var m = media(lista);

                        //determinar hubs
                        var qtdeHubs = 0;

                        for (var node of json.nodes) {
                            node.hub = node.connections >= m;
                            
                            if (node.hub) {
                                qtdeHubs++;
                            }
                        }

                        if (qtdeHubs > 0) {
                            json.nodes = json.nodes.sort(function(node, node2) {
                                return node2.connections - node.connections;
                            });

                            for (var node of json.nodes) {
                                if (node.hub) {
                                    var fpp;
                                    var percepcao;

                                    var group = json.clusters.find(function(g) {
                                        return g.id == node.group;
                                    });

                                    //NC
                                    if (group.X == 0) {
                                        //CT
                                        if (group.Y == 0) {
                                            fpp = 'Sentimento';
                                            percepcao = 'Intuição';
                                        }
                                        //NC
                                        else if (group.Y == 2) {
                                            fpp = 'Sentimento';
                                            percepcao = 'Intuição';
                                        }
                                    }
                                    //CT
                                    else if (group.X == 2) {
                                        //CT
                                        if (group.Y == 0) {
                                            fpp = 'Pensamento';
                                            percepcao = 'Percepção';
                                        }
                                        //NC
                                        else if (group.Y == 2) {
                                            fpp = 'Pensamento';
                                            percepcao = 'Percepção';
                                        }
                                    }
                                }
                                
                                break;
                            }

                            if (fpp && percepcao) {
                                showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Classificação do agente: " + fpp + " / " + percepcao + ".");
                            }
                            else {
                                showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Uma nova coleta deve ser feita.");
                            }
                        }
                        else {
                            showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Nenhum hub foi encontrado.");
                        }
                    }
                    else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> Nenhum conceito foi adicionado ao MCE.");
                    }
                });
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
        
        function carregarDados(url, callback) {
            $.ajax({
                url: url,
                success: function(data) {
                    callback(data);
                },
                error: function() {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Infelizmente não foi possível carregar os dados para a análise. <br />Por favor, tente novamente em instantes.");
                }
            });
        }
        
        function media(s) {
            var sum = 0;
            var i = 0;
            var n = s.length;

            for(i = 0; i < n; i++) {
                sum += s[i];
            }

            return sum / n;
        }

        function variancia(s) {
            var sum = 0;
            var dev = 0;
            var med = media(s);
            var i = 0;
            var n = s.length;

            for(i = 0; i < n; i++) {
                dev = s[i] - med;
                sum += Math.pow(dev, 2);
            }

            return sum / n;
        }

        function desvpad(s) {
            var v = variancia(s);
            return Math.sqrt(v);
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
            <div class="box">
                <form class="form-default" onsubmit="return false">
                    <button type="button" class="btn btn-info" id="btnAnaliseContCc">Análise por contagem <br /> e frequência de Cc</button>
                    <button type="button" class="btn btn-info" id="btnFuncPsico">Identificação da função <br /> psicológica</button>
                </form>    
            </div>
        </div>
    </div>
</tmpl:dashboard>