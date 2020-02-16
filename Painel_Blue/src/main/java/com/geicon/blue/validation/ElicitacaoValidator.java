package com.geicon.blue.validation;

import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação das elicitações
 *
 * @author Gabriel Duarte
 */
public class ElicitacaoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param elicitacao Elicitacao validada
     * @param pesquisaAtual Pesquisa atual
     */
    public ElicitacaoValidator(Elicitacao elicitacao, Pesquisa pesquisaAtual) {
        if (elicitacao != null) {
            add(elicitacao.getBase(), "A base de conhecimento não foi escolhida;");
            add(elicitacao.getAgente(), "O agente não foi escolhido;");

            if (elicitacao.getBase() != null) {
                add(elicitacao.getBase().getPesquisa().equals(pesquisaAtual), "A base selecionada não pertence a pesquisa ativa;");
            }

            if (elicitacao.getAgente() != null) {
                add(elicitacao.getAgente().getBase().getPesquisa().equals(pesquisaAtual), "O agente selecionado não pertence a pesquisa ativa;");
            }
        }
        else {
            add(elicitacao, "Os dados necessários não foram enviados");
        }
    }
}
