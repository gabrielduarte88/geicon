package com.geicon.blue.validation;

import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos objetos
 *
 * @author Gabriel Duarte
 */
public class ObjetoAtualizacaoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param objeto Objeto
     */
    public ObjetoAtualizacaoValidator(Objeto objeto) {
        if (objeto != null) {
            add(objeto.getNome(), "O nome do objeto não foi informado;");
        }
        else {
            add(objeto, "Os dados necessários não foram enviados");
        }
    }
}
