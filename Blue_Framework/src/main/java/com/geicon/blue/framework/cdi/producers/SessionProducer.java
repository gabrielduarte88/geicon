package com.geicon.blue.framework.cdi.producers;

import com.geicon.blue.framework.persistence.util.HibernateUtil;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import org.hibernate.Session;

/**
 * Gerente das Hibernate session
 *
 * @author Gabriel Duarte
 */
public class SessionProducer {
    /**
     * Cria uma nova Hibernate session
     *
     * @return Hibernate session
     */
    @Produces
    @RequestScoped
    public Session openSession() {
        return HibernateUtil.getSession("hibernate.cfg.xml");
    }

    /**
     * Finaliza a sessão
     *
     * @param session Sessão
     */
    public void close(@Disposes Session session) {
        session.close();
    }
}
