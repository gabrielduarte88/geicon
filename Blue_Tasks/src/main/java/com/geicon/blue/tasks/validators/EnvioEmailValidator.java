package com.geicon.blue.tasks.validators;

import com.geicon.blue.api.models.EnvioEmail;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação de envios de e-mail
 *
 * @author Gabriel Duarte
 */
public class EnvioEmailValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param envio Envio validado
     */
    public EnvioEmailValidator(EnvioEmail envio) {
        if (envio != null) {
            add((envio.getEmailDestino() != null && !envio.getEmailDestino().isEmpty())
                    || (envio.getEmailDestinoCC() != null && !envio.getEmailDestinoCC().isEmpty())
                    || (envio.getEmailDestinoCCO() != null && !envio.getEmailDestinoCCO().isEmpty()), "E-mails de destino não informados.");

            add(envio.getAssunto(), "Assunto não informado;");
        }
        else {
            add(envio, "Os dados necessarios não foram enviados.");
        }
    }
}
