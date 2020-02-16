package com.geicon.blue.validation;

import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação das relações
 *
 * @author Gabriel Duarte
 */
public class RelacaoPesoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param relacao Relação validada
     */
    public RelacaoPesoValidator(Relacao relacao) {
        if (relacao != null) {
            add(relacao.getPeso(), "O peso da relação não foi definido;");
        }
        else {
            add(relacao, "Os dados necessários não foram enviados");
        }
    }
}
