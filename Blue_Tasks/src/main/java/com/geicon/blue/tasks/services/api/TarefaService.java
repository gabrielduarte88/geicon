package com.geicon.blue.tasks.services.api;

import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Set;

/**
 * Interface Service - Tarefa
 *
 * @author Gabriel Duarte
 */
public interface TarefaService extends Service<Tarefa> {
    /**
     * Contar as tarefas pendentes
     *
     * @return Número de tarefas pendentes
     */
    public int contarTarefasPendentes();

    /**
     * Listar as tarefas pendentes
     *
     * @return Lista de tarefas pendentes
     */
    public Set<Tarefa> listarTarefasPendentes();

    /**
     * Listar tarefas antigas
     *
     * @return Lista de tarefas antigas
     */
    public Set<Tarefa> listarTarefasAntigas();
}
