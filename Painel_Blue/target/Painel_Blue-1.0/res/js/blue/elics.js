var Elics = function (elicitation, options) {
    var elics = this;

    this.elicitation = elicitation;

    this.opts = $.extend({
        'contextPath': '/',
        'agent': 'Agente',
        'domain': 'Domain',
        'editable': true
    }, options);

    this.init = function () {
        $('div.documento>p>span.word').click(function (evt) {
            elics.selectConcept($(this), evt);
        });
        $('div.documento>p>span.word').dblclick(function (evt) {
            elics.addVerb();
            evt.stopPropagation();
        });

        $('body').click(function () {
            $('div.documento>p>span.selected').removeClass('selected');
        });

        elics.showPropositions();
        elics.showConcepts();
    };

    this.showPropositions = function () {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes',
            data: {
                editable: this.opts.editable
            },
            success: function (data) {
                if (data != null) {
                    $('div.proposicoes-container').html(data);
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.showConcepts = function () {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/conceitos',
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.selectConcept = function (el, evt) {
        var next = false;
        var idx = el.attr('ref') * 1;
        var selectedEls = $('div.documento>p>span.selected');

        if (selectedEls.length == 0) {
            next = true;
        }
        else {
            selectedEls.each(function () {
                var elIdx = $(this).attr('ref') * 1;
                if (elIdx == idx - 1 || elIdx == idx + 1) {
                    next = true;
                }
            });
        }

        if (!next) {
            $('div.documento>p>span.selected').removeClass('selected');
        }

        el.toggleClass('selected');
        evt.stopPropagation();
    };

    this.addVerb = function () {
        var words = '';
        var document;
        var begin;
        var end;
        $('div.documento>p>span.selected').each(function () {
            words += $(this).text() + ' ';
            if (!begin) {
                begin = $(this).attr('ref');
                document = $(this).attr('doc');
            }
            end = $(this).attr('ref');
        });

        elics.verifyAddVerb(words.trim(), document, begin, end);

        $('div.documento>p>span.selected').removeClass('selected');
    };

    this.verifyAddVerb = function (verb, document, begin, end) {
//        if (verb != '') {
            var form = $('<form>').submit(function () {
                return false;
            });
            form.append($('<label>').text("Adicionar verbo"));
            form.append($('<input>').attr('type', 'text').attr('maxlength', '60').addClass('form-control').val(verb));
            var buttons = $('<div>').addClass('buttons');
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                    .click(function () {
                        $.unblockUI();
                    }));
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-success').html('Inserir')
                    .click(function () {
                        return function (form, document, begin, end) {
                            elics.doAddVerb(form.find('input[type=text]').val(), document, begin, end);
                            $.unblockUI();
                        }(form, document, begin, end);
                    }));
            showModal(form, buttons);
//        }
//        else {
//            showModal("<i class=\"fa fa-warning\"></i> <br /> Escolha um verbo.");
//        }
    };

    this.doAddVerb = function (verb, document, begin, end, callback) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/adicionar',
            data: {
                'relacao.nome': verb,
                'relacao.documento.code': document,
                'relacao.posicaoInicial': begin,
                'relacao.posicaoFinal': end
            },
            success: function (data) {
                if (data != null) {
                    $('div.proposicoes-container').html(data);
                    
                    if (callback) {
                        var ref = $('div.proposicoes-container').find('div.proposicao:last').attr('ref');
                        callback(ref);
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.changeVerb = function (input) {
        var parent = input.closest('div.proposicao');

        var proposition = parent.attr('ref');
        var verb = parent.attr('titulo');

        elics.verifyChangeVerb(proposition, verb, input.val());
    };

    this.verifyChangeVerb = function (proposition, verb, newVerb) {
        if (verb != newVerb) {
            var buttons = $('<div>').addClass('buttons');
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                    .click(function () {
                        $.unblockUI();
                    }));
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-success').html('Alterar')
                    .click(function () {
                        return function (proposition, newVerb) {
                            $.unblockUI();
                            elics.doChangeVerb(proposition, newVerb);
                        }(proposition, newVerb);
                    }));
            showModal("Deseja realmente alterar o núcleo da proposição de '" + verb + "' para '" + newVerb + "'?", buttons);
        }
    };

    this.doChangeVerb = function (proposition, newVerb) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/alterar',
            data: {
                'relacao.code': proposition,
                'relacao.nome': newVerb
            },
            success: function (data) {
                if (data != null) {
                    $('div.proposicoes-container').html(data);
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.highlightVerb = function (proposition) {
        var doc = proposition.attr('docId');
        var begin = proposition.attr('inicial') * 1;
        var end = proposition.attr('final') * 1;

        var document = $('div.documento#' + doc + '>p');

        $('span', document).removeClass('highlight');

        for (var i = begin; i <= end; i++) {
            $('span[ref=' + i + ']', document).addClass('highlight');
        }
    };

    this.unhighlightVerb = function () {
        $('div.documento>p>span').removeClass('highlight');
    };

    this.addConcept = function (input) {
        var proposition = input.parent().attr('ref');
        var words = '';
        var document;
        var begin;
        var end;

        $('div.documento>p>span.selected').each(function () {
            words += $(this).text() + ' ';

            if (!begin) {
                begin = $(this).attr('ref');
                document = $(this).attr('doc');
            }
            end = $(this).attr('ref');
        });

        elics.verifyConcept(proposition, input.hasClass('causa') ? 'causa' : 'efeito', words.trim(), document, begin, end);

        $('div.documento>pre>span.selected').removeClass('selected');
    };

    this.verifyConcept = function (proposition, position, concept, document, begin, end) {
//        if (concept != '') {
            var form = $('<form>').submit(function () {
                return false;
            });
            form.append($('<label>').text("Adicionar substantivo"));
            form.append($('<input>').attr('type', 'text').attr('maxlength', '60').addClass('form-control').val(concept));
            var buttons = $('<div>').addClass('buttons');
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                    .click(function () {
                        $.unblockUI();
                    }));
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-success').html('Inserir')
                    .click(function () {
                        return function (proposition, position, form, concept, document, begin, end) {
                            elics.doAddConcept(proposition, position, concept != '' ? concept : form.find('input[type=text]').val(), form.find('input[type=text]').val(), document, begin, end);
                            $.unblockUI();
                        }(proposition, position, form, concept, document, begin, end);
                    }));
            showModal(form, buttons);
//        }
//        else {
//            showModal("<i class=\"fa fa-warning\"></i> <br /> Escolha um conceito.");
//        }
    };

    this.doAddConcept = function (proposition, position, concept, occurence, document, begin, end, callback) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/adicionarConceito', data: {
                'relacao.code': proposition,
                'posicao': position,
                'objeto.nome': concept,
                'ocorrencia.valor': occurence,
                'ocorrencia.documento.code': document,
                'ocorrencia.posicaoInicial': begin,
                'ocorrencia.posicaoFinal': end
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    elics.showPropositions();
                    
                    if (callback) {
                        var ref = $('div.conceitos-container').find('ul.conceito>li:last').attr('ref');
                        callback(ref);
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.defineWeight = function (button) {
        var parent = button.closest('div.proposicao');
        var cause = parent.find('span.causa').text();
        var verb = parent.attr('titulo');
        var effect = parent.find('span.efeito').text();

        var proposition = parent.attr('ref');

        elics.verifyDefineWeight(proposition, cause, verb, effect);
    };

    this.verifyDefineWeight = function (proposition, cause, verb, effect) {
        var opts = $('<div>').addClass('onbody');
        
        opts.append($('<h3>').html('Defina a influência do Conceito Causa sobre o Conceito efeito'));
        
        var opt1 = $('<div>');
        var opt2 = $('<div>');
        
        opt1.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-danger').html('+')
                .click(function () {
                    return function (proposition) {
                        $.unblockUI();
                        elics.doDefineWeight(proposition, 'R');
                    }(proposition);
                }));
                
        opt1.append($('<p>').html('Um aumento do Conceito Causa provoca um aumento no Conceito Efeito'));
        
        opts.append(opt1);
                
        opt2.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-active').html('-')
                .click(function () {
                    return function (proposition) {
                        $.unblockUI();
                        elics.doDefineWeight(proposition, 'B');
                    }(proposition);
                }));
                
        opt2.append($('<p>').html('Um aumento do Conceito Causa provoca uma diminuição no Conceito Efeito'));        
                
        opts.append(opt2);
        
        var buttons = $('<div>').addClass('buttons');
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                .click(function () {
                    $.unblockUI();
                }));
        
        showModal(opts, buttons);
    };

    this.doDefineWeight = function (proposition, weight, callback) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/definirPeso', data: {
                'relacao.code': proposition,
                'relacao.peso': weight
            },
            success: function (data) {
                if (data != null) {
                    $('div.proposicoes-container').html(data);
                    
                    if (callback) {
                        callback();
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.removeProposition = function (button) {
        var ref = button.attr('ref');
        var title = button.closest('div.proposicao').attr('titulo');

        elics.verifyRemoveProposition(ref, title);
    };

    this.verifyRemoveProposition = function (proposition, verb) {
        var buttons = $('<div>').addClass('buttons');
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                .click(function () {
                    $.unblockUI();
                }));
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-danger').html('Remover')
                .click(function () {
                    return function (proposition) {
                        $.unblockUI();
                        elics.doRemoveProposition(proposition);
                    }(proposition);
                }));
        showModal("Deseja realmente remover a proposição '" + verb + "'?", buttons);
    };

    this.doRemoveProposition = function (proposition) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/remover', data: {
                'relacao.code': proposition
            },
            success: function (data) {
                if (data != null) {
                    $('div.proposicoes-container').html(data);
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };
    
    this.highlightConcept = function (concept) {
        var doc = concept.attr('docId');
        var begin = concept.attr('inicial') * 1;
        var end = concept.attr('final') * 1;

        var document = $('div.documento#' + doc + '>p');

        $('span', document).removeClass('highlight');

        for (var i = begin; i <= end; i++) {
            $('span[ref=' + i + ']', document).addClass('highlight');
        }
    };

    this.unhighlightConcept = function () {
        $('div.documento>p>span').removeClass('highlight');
    };

    this.mergeConcepts = function (element, target) {
        var conceptRef = element.attr('ref');
        var targetRef = target.attr('ref');
        var conceptId = element.attr('id');
        var targetId = target.attr('objectId');
        var concept = element.attr('titulo');
        var concept2 = target.attr('titulo');

        elics.verifyMergeConcepts(conceptRef, targetRef, conceptId, targetId, concept, concept2);
    };

    this.verifyMergeConcepts = function (conceptRef, targetRef, conceptId, targetId, concept1, concept2) {
        if (conceptId != targetId) {
            var buttons = $('<div>').addClass('buttons');
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                    .click(function () {
                        $.unblockUI();
                    }));
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-success').html('Mesclar')
                    .click(function () {
                        return function (conceptRef, targetRef) {
                            $.unblockUI();
                            elics.doMergeConcepts(conceptRef, targetRef);
                        }(conceptRef, targetRef);
                    }));
            showModal("Atenção! Deseja realmente mesclar os objetos '" + concept1 + "' e '" + concept2 + "' ? Esse processo é irreversível.", buttons);
        }
        else {
            showModal("<i class=\"fa fa-warning\"></i> <br /> Os objetos de origem e destino são o mesmo objeto.");
        }
    };

    this.doMergeConcepts = function (conceptId, targetId) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/proposicoes/mesclarConceitos', data: {
                'objeto1.code': conceptId,
                'objeto2.code': targetId
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    elics.showPropositions();
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.changeConcept = function (input) {
        var parent = input.closest('ul.correspondencia');

        var conceptId = parent.attr('ref');
        var concept = parent.attr('titulo');
        var newConcept = input.val();

        elics.verifyChangeConcept(conceptId, concept, newConcept);
    };

    this.verifyChangeConcept = function (conceptId, concept, newConcept) {
        if (concept != newConcept) {
            var buttons = $('<div>').addClass('buttons');
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                    .click(function () {
                        $.unblockUI();
                    }));
            buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-success').html('Alterar')
                    .click(function () {
                        return function (conceptId, newConcept) {
                            $.unblockUI();
                            elics.doChangeConcept(conceptId, newConcept);
                        }(conceptId, newConcept);
                    }));
            showModal("Deseja realmente alterar o conceito entitulado '" + concept + "' para '" + newConcept + "'?", buttons);
        }
    };

    elics.doChangeConcept = function (conceptId, newConcept) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/conceitos/alterar', data: {
                'objeto.code': conceptId,
                'objeto.nome': newConcept
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    elics.showPropositions();
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.defineConceptPositionByAgent = function (button) {
        var parent = button.parent();

        var concept = parent.attr('ref');
        var title = parent.attr('titulo');

        elics.verifyDefineConceptPositionByAgent(concept, title);
    };

    this.verifyDefineConceptPositionByAgent = function (concept, title) {
        var opts = $('<div>').addClass('onbody');
        
        opts.append($('<h3>').html('Controle do agente sobre o conceito'));
        
        var opt1 = $('<div>');
        var opt2 = $('<div>');
        var opt3 = $('<div>');
        
        //OPT1
        opt1.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('ct').html('CT')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByAgent(concept, 'CT');
                    }(concept);
                }));
        
        opt1.append($('<p>').html('O agente tem certeza que controla o conceito'));
        
        opts.append(opt1);
                
        //OPT2
        opt2.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('pn').html('PN')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByAgent(concept, 'PN');
                    }(concept);
                }));
                
        opt2.append($('<p>').html('O agente não tem certeza que controla o conceito'));        
                
        opts.append(opt2);
        
        //OPT3
        opt3.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('nc').html('NC')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByAgent(concept, 'NC');
                    }(concept);
                }));
                
        opt3.append($('<p>').html('O agente tem certeza que não controla o conceito'));        
                
        opts.append(opt3);
        
        var buttons = $('<div>').addClass('buttons');
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                .click(function () {
                    $.unblockUI();
                }));
        
        showModal(opts, buttons);
    };

    this.doDefineConceptPositionByAgent = function (concept, position, callback) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/conceitos/definirPosicaoAgente', data: {
                'objeto.code': concept,
                'objeto.controlabilidadeAgente': position
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    
                    if (callback) {
                        callback();
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };

    this.defineConceptPositionByDomain = function (button) {
        var parent = button.parent();

        var concept = parent.attr('ref');
        var title = parent.attr('titulo');

        elics.verifyDefineConceptPositionByDomain(concept, title);
    };

    this.verifyDefineConceptPositionByDomain = function (concept, title) {
        var opts = $('<div>').addClass('onbody');
        
        opts.append($('<h3>').html('Mudanças que os conceitos provocam no cenário e que podem contribuir para atingir o objetivo'));
        
        var opt1 = $('<div>');
        var opt2 = $('<div>');
        var opt3 = $('<div>');
        
        //OPT1
        opt1.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('ct').html('CT')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByDomain(concept, 'CT');
                    }(concept);
                }));
        
        opt1.append($('<p>').html('O agente tem certeza que controla o conceito'));
        
        opts.append(opt1);
                
        //OPT2
        opt2.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('pn').html('PN')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByDomain(concept, 'PN');
                    }(concept);
                }));
                
        opt2.append($('<p>').html('O agente não tem certeza que controla o conceito'));        
                
        opts.append(opt2);
        
        //OPT3
        opt3.append($('<button>').attr('type', 'button').addClass('btn').addClass('full').addClass('nc').html('NC')
                .click(function () {
                    return function (concept) {
                        $.unblockUI();
                        elics.doDefineConceptPositionByDomain(concept, 'NC');
                    }(concept);
                }));
                
        opt3.append($('<p>').html('O agente tem certeza que não controla o conceito'));        
                
        opts.append(opt3);
        
        var buttons = $('<div>').addClass('buttons');
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                .click(function () {
                    $.unblockUI();
                }));
        
        showModal(opts, buttons);
    };

    this.doDefineConceptPositionByDomain = function (concept, position, callback) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/conceitos/definirPosicaoDominio', data: {
                'objeto.code': concept,
                'objeto.controlabilidadeDominio': position
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    
                    if (callback) {
                        callback();
                    }
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };
    
    this.removeConcept = function(button) {
        var ref = button.closest('ul.correspondencia').attr('ref');
        var title = button.closest('ul').attr('titulo');

        elics.verifyRemoveConcept(ref, title);
    };
    
    this.verifyRemoveConcept = function(conceptId, concept) {
        var buttons = $('<div>').addClass('buttons');
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-default').html('Cancelar')
                .click(function () {
                    $.unblockUI();
                }));
        buttons.append($('<button>').attr('type', 'button').addClass('btn').addClass('btn-danger').html('Remover')
                .click(function () {
                    return function (conceptId) {
                        $.unblockUI();
                        elics.doRemoveConcept(conceptId);
                    }(conceptId);
                }));
        showModal("Deseja realmente remover o conceito '" + concept + "'?", buttons);
    };

    this.doRemoveConcept = function(concept) {
        $.ajax({type: 'POST',
            url: (elics.opts.contextPath) + '/elicitacoes/' + elics.elicitation + '/conceitos/remover', data: {
                'objeto.code': concept
            },
            success: function (data) {
                if (data != null) {
                    $('div.conceitos-container').html(data);
                    elics.showPropositions();
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas houve um erro nos serviços internos do sistema. <br />Por favor, tente novamente em instantes.");
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas os serviços internos do sistema não estão disponíveis no momento. <br />Por favor, tente novamente em instantes.");
            }
        });
    };
};