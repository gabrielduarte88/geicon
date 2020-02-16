package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Log;
import com.geicon.blue.framework.persistence.services.Service;

/**
 * Interface Service - Log
 *
 * @author Gabriel Duarte
 */
public interface LogService extends Service<Log> {
    /**
     * Efetuar registro de atividade
     *
     * @param instituicao Instituição
     * @param registro Registro
     * @param dados Dados do registro
     */
    public void log(Instituicao instituicao, String registro, Object... dados);
}
