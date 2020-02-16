package com.geicon.blue.validation;

import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação de bases de conhecimento
 *
 * @author Gabriel Duarte
 */
public class BaseConhecimentoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param baseConhecimento Base de conhecimento validada
     */
    public BaseConhecimentoValidator(BaseConhecimento baseConhecimento) {
        if (baseConhecimento != null) {
            add(baseConhecimento.getTitulo(), "O campo 'título' não foi preenchido;");
            add(baseConhecimento.getProposicaoInicial(), "O campo 'proposição inicial' não foi preenchido;");
            add(baseConhecimento.getDescricao(), "O campo 'descrição' não foi preenchido;");
        }
        else {
            add(baseConhecimento, "Os dados necessários não foram enviados");
        }
    }
}
