package com.geicon.blue.validation;

import com.geicon.blue.api.models.Interesse;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Validação de interesse
 *
 * @author Gabriel Duarte
 */
public class InteresseValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param interesse Interesse validado
     */
    public InteresseValidator(Interesse interesse) {
        if (interesse != null) {
            add(interesse.getNome(), "O campo 'nome' não foi preenchido;");
            add(interesse.getEmail(), "O campo 'e-mail' não foi preenchido;");
            add(interesse.getOrganizacao(), "O campo 'organização/instituição' não foi preenchido;");

            if (!hasErrors()) {
                add(EmailValidator.getInstance().isValid(interesse.getEmail()), "Necessário informar um e-mail válido.");
            }
        }
        else {
            add(interesse, "Os dados necessários não foram enviados");
        }
    }
}
