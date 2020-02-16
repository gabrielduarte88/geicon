package com.geicon.blue.validation;

import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Validação do usuário
 *
 * @author Gabriel Duarte
 */
public class UsuarioValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param usuario Usuário validado
     */
    public UsuarioValidator(Usuario usuario) {
        if (usuario != null) {
            add(usuario.getNome(), "O campo 'nome' não foi preenchido;");
            add(usuario.getEmail(), "O campo 'e-mail' não foi preenchido;");
            add(usuario.getCelular(), "O campo 'celular' não foi preenchido;");

            if (!hasErrors()) {
                add(EmailValidator.getInstance().isValid(usuario.getEmail()), "O e-mail informado não é válido;");
            }
        }
        else {
            add(usuario, "Os dados necessários não foram enviados");
        }
    }
}
