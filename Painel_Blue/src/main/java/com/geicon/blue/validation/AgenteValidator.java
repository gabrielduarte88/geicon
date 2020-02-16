package com.geicon.blue.validation;

import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos agentes
 *
 * @author Gabriel Duarte
 */
public class AgenteValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param agente Agente validado
     * @param pesquisaAtual Pesquisa atual
     * @param atualizacao Indicador de atualização
     */
    public AgenteValidator(Agente agente, Pesquisa pesquisaAtual, boolean atualizacao) {
        if (agente != null) {
            if (!atualizacao) {
                add(agente.getBase(), "A base de conhecimento não foi escolhida;");
            }

            add(agente.getNome(), "O campo 'nome' não foi preenchido;");
            add(agente.getDescricao(), "O campo 'descrição' não foi preenchido;");

            if (agente.getBase() != null) {
                add(agente.getBase().getPesquisa().equals(pesquisaAtual), "A base selecionada não pertence a pesquisa ativa;");
            }
        }
        else {
            add(agente, "Os dados necessários não foram enviados");
        }
    }
}
