package com.geicon.blue.validation;

import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos objetos
 *
 * @author Gabriel Duarte
 */
public class ObjetoControlabilidadeDominioValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param objeto Objeto
     */
    public ObjetoControlabilidadeDominioValidator(Objeto objeto) {
        if (objeto != null) {
            add(objeto.getControlabilidadeDominio(), "A controlabilidade do objeto para o domínio não foi informada;");
        }
        else {
            add(objeto, "Os dados necessários não foram enviados");
        }
    }
}
