package com.geicon.blue.validation;

import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação das relações
 *
 * @author Gabriel Duarte
 */
public class RelacaoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param relacao Relação validada
     * @param atualizacao Indicador de atualização
     */
    public RelacaoValidator(Relacao relacao, boolean atualizacao) {
        if (relacao != null) {
            add(relacao.getNome(), "O campo 'nome' não foi preenchido;");

//            if (!atualizacao) {
//                add(relacao.getDocumento(), "O documento não foi escolhido;");
//            }
        }
        else {
            add(relacao, "Os dados necessários não foram enviados");
        }
    }
}
