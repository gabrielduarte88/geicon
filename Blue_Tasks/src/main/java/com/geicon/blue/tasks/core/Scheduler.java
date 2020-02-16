package com.geicon.blue.tasks.core;

import com.geicon.blue.tasks.api.TaskManager;
import com.geicon.blue.tasks.util.BeanManagerUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.context.bound.BoundRequestContext;

/**
 * Agendador
 *
 * @author Gabriel Duarte
 */
@ApplicationScoped
public class Scheduler extends TimerTask implements Serializable {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indicador de atividade
     */
    private boolean active;
    /**
     * Contexto
     */
    @Inject
    private BoundRequestContext requestContext;
    /**
     * Gerente de beans
     */
    @Inject
    private BeanManager manager;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Construtor
     */
    public Scheduler() {
        active = true;
    }

    /**
     * Inicializa o scheduler
     */
    public void init() {
    }

    /**
     * Obtem o indicador de atividade
     *
     * @return Indicador de atividade
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Altera o indicador de atividade
     *
     * @param active Indicador de atividade
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        if (active) {
            logger.info("Ativando gerenciador não de tarefas.");

            Map<String, Object> contextData = new HashMap<>(0);

            requestContext.associate(contextData);
            requestContext.activate();

            TaskManager taskManager = BeanManagerUtil.getBeanInstance(manager, TaskManager.class, RequestScoped.class);

            if (taskManager.hasJobs()) {
                taskManager.executeJobs();
            }

            try {
                requestContext.invalidate();
                requestContext.deactivate();
            }
            finally {
                requestContext.dissociate(contextData);
            }

            logger.info("Desativando gerenciador não de tarefas.");
        }
        else {
            cancel();
        }
    }
}
