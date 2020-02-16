package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Interesse;
import com.geicon.blue.framework.persistence.services.Service;

/**
 * Interface Service - Interesse
 *
 * @author Gabriel Duarte
 */
public interface InteresseService extends Service<Interesse> {
    /**
     * Inserir interesse
     *
     * @param interesse Interesse
     */
    public void inserirInteresse(Interesse interesse);

    /**
     * Verificar interesse existente
     *
     * @param interesse Interesse
     * @return true se existir / false se não
     */
    public boolean verificarInteresseDuplicado(Interesse interesse);
}
