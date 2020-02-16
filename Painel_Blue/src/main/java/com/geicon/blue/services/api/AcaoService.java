package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Acao;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Set;

/**
 * Interface Service - Acao
 *
 * @author Gabriel Duarte
 */
public interface AcaoService extends Service<Acao> {
    /**
     * Listar ações
     *
     * @return Lista de ações
     */
    public Set<Acao> listarAcoes();

    /**
     * Buscar ação por ID
     *
     * @param id ID
     * @return Ação encontrada
     */
    public Acao buscarAcaoPorId(Integer id);
}
