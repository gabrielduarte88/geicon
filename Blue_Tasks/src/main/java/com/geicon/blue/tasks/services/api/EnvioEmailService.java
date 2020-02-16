package com.geicon.blue.tasks.services.api;

import com.geicon.blue.api.models.EnvioEmail;
import com.geicon.blue.framework.persistence.services.Service;

/**
 * Interface Service - EnvioEmail
 *
 * @author Gabriel Duarte
 */
public interface EnvioEmailService extends Service<EnvioEmail> {
    /**
     * Buscar envio de e-mail por ID
     *
     * @param id ID
     * @return Envio encontrado
     */
    public EnvioEmail buscarEnvioPorId(int id);
}
