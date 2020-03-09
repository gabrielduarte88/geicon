function analisarContCc(contextPath, codigo, callback) {
    var url = contextPath + '/elicitacoes/' + codigo + '/dados';
    carregarDados(url, function (json) {
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
            var lista = json.nodes.map(function (node) {
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
                json.nodes = json.nodes.sort(function (node, node2) {
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

                        var group = json.clusters.find(function (g) {
                            return g.id == node.group;
                        });

                        switch (group.X) {
                            case 0:
                            {
                                teoricoPragmatico = 'Teórico';
                                teoricos++;
                                break;
                            }
                            case 1:
                            {
                                teoricoPragmatico = 'Teórico-Pragmático';
                                teoricos++;
                                pragmaticos++;
                                break;
                            }
                            case 2:
                            {
                                teoricoPragmatico = 'Pragmático';
                                pragmaticos++;
                                break;
                            }
                        }

                        switch (group.Y) {
                            case 0:
                            {
                                ativoReflexivo = 'Ativo';
                                ativos++;
                                break;
                            }
                            case 1:
                            {
                                ativoReflexivo = 'Ativo-Reflexivo';
                                ativos++;
                                reflexivos++;
                                break;
                            }
                            case 2:
                            {
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

                if (callback) {
                    callback(teoricoPragmatico + " / " + ativoReflexivo);
                } else {
                    showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Classificação do agente: " + teoricoPragmatico + " / " + ativoReflexivo + ".");
                }
            } else {
                if (callback) {
                    callback('');
                } else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Nenhum hub foi encontrado.");
                }
            }
        } else {
            if (callback) {
                callback('');
            } else {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Nenhum conceito foi adicionado ao MCE.");
            }
        }
    });
}

function analisarFuncPsico(contextPath, codigo, callback) {
    var url = contextPath + '/elicitacoes/' + codigo + '/dados';

    carregarDados(url, function (json) {
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
            var lista = json.nodes.map(function (node) {
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
                json.nodes = json.nodes.sort(function (node, node2) {
                    return node2.connections - node.connections;
                });

                for (var node of json.nodes) {
                    if (node.hub) {
                        var fpp;
                        var percepcao;

                        var group = json.clusters.find(function (g) {
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
                    if (callback) {
                        callback(fpp + " / " + percepcao);
                    } else {
                        showModal("<i class=\"fa fa-thumbs-o-up\"></i> <br /> Classificação do agente: " + fpp + " / " + percepcao + ".");
                    }
                } else {
                    if (callback) {
                        callback('');
                    } else {
                        showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Uma nova coleta deve ser feita.");
                    }
                }
            } else {
                if (callback) {
                    callback('');
                } else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> O mapa conceitual não possui dados suficientes para a análise. Nenhum hub foi encontrado.");
                }
            }
        } else {
            if (callback) {
                callback('');
            } else {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Nenhum conceito foi adicionado ao MCE.");
            }
        }
    });
}

function carregarDados(url, callback) {
    $.ajax({
        url: url,
        success: function (data) {
            callback(data);
        },
        error: function () {
            showModal("<i class=\"fa fa-warning\"></i> <br /> Infelizmente não foi possível carregar os dados para a análise. <br />Por favor, tente novamente em instantes.");
        }
    });
}

function media(s) {
    var sum = 0;
    var i = 0;
    var n = s.length;

    for (i = 0; i < n; i++) {
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

    for (i = 0; i < n; i++) {
        dev = s[i] - med;
        sum += Math.pow(dev, 2);
    }

    return sum / n;
}

function desvpad(s) {
    var v = variancia(s);
    return Math.sqrt(v);
}