package com.geicon.blue.validation;

import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação de pesquisa
 *
 * @author Gabriel Duarte
 */
public class PesquisaValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param pesquisa Pesquisa validada
     * @param administrador Administrador
     */
    public PesquisaValidator(Pesquisa pesquisa, boolean administrador) {
        if (pesquisa != null) {
            add(pesquisa.getNome(), "O campo 'nome' não foi preenchido;");

            if (administrador) {
                add(pesquisa.getResponsavel(), "O responsável não foi escolhido;");
            }
        }
        else {
            add(pesquisa, "Os dados necessários não foram enviados");
        }
    }
}
