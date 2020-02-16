package com.geicon.blue.tasks.core;

import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.tasks.api.TaskManager;
import com.geicon.blue.tasks.services.api.TarefaService;
import java.io.Serializable;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Gerenciador de tarefas
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class BlueTaskManager implements TaskManager, Serializable {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Serviços de tarefas
     */
    @Inject
    private TarefaService tarefaService;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    @Override
    public synchronized boolean hasJobs() {
        int tarefasPendentes = tarefaService.contarTarefasPendentes();

        logger.info("Tarefas pendentes: " + tarefasPendentes);

        return tarefasPendentes > 0;
    }

    @Override
    public synchronized void executeJobs() {
        Set<Tarefa> tarefasPendentes = tarefaService.listarTarefasPendentes();

        try {
            for (Tarefa tarefa : tarefasPendentes) {
                if (tarefa.getMetodo() != null) {
                    switch (tarefa.getMetodo()) {
                        case "envioEmail": {
                            logger.info("Enviando os e-mails pendentes.");

                            break;
                        }
                    }
                }
            }
        }
        catch (RuntimeException ex) {
            logger.error("Houve um erro não esperado.", ex);
        }

        Set<Tarefa> tarefasAntigas = tarefaService.listarTarefasAntigas();

        for (Tarefa tarefa : tarefasAntigas) {
            tarefaService.delete(tarefa);
        }
    }
}
