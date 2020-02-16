package com.geicon.blue.validation;

import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos objetos
 *
 * @author Gabriel Duarte
 */
public class ObjetoControlabilidadeAgenteValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param objeto Objeto
     */
    public ObjetoControlabilidadeAgenteValidator(Objeto objeto) {
        if (objeto != null) {
            add(objeto.getControlabilidadeAgente(), "A controlabilidade do objeto pelo agente não foi informada;");
        }
        else {
            add(objeto, "Os dados necessários não foram enviados");
        }
    }
}
