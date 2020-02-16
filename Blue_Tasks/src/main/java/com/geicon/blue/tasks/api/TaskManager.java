package com.geicon.blue.tasks.api;

/**
 * Gerente de tarefas
 *
 * @author Gabriel Duarte
 */
public interface TaskManager {
    /**
     * Verifica se existem tarefas na fila
     *
     * @return true se houverem / false se não
     */
    public boolean hasJobs();

    /**
     * Executa as tarefas pendentes
     */
    public void executeJobs();
}
