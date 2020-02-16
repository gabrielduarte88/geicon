package com.geicon.blue.tasks.servlets;

import com.geicon.blue.tasks.core.Scheduler;
import java.util.Timer;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet de inicializacao do agendamento
 *
 * @author Gabriel Duarte
 */
@WebServlet(name = "ScheduleInitServlet", urlPatterns = "/sys/scheduler", loadOnStartup = 2)
public class ScheduleInitServlet extends HttpServlet {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * Agendador
     */
    @Inject
    private Scheduler scheduler;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Inicialização
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        scheduler.init();

        Timer timer = new Timer();
        timer.schedule(scheduler, 10 * 1000, 5 * 1000);

        logger.info("Agendador online.");
    }

    /**
     * Finalizacao
     */
    @Override
    public void destroy() {
        if (scheduler != null && scheduler.isActive()) {
            logger.info("Finalizando agendador.");

            scheduler.setActive(false);

            logger.info("Agendador finalizado.");
        }
    }
}
