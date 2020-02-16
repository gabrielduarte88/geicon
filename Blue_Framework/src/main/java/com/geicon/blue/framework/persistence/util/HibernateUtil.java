package com.geicon.blue.framework.persistence.util;

import com.geicon.blue.framework.exceptions.IllegalParametersException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Util
 *
 * @author Gabriel Duarte
 */
public final class HibernateUtil {
    /**
     * Fábrica de sessões
     */
    private static SessionFactory sessionFactory;

    /**
     * Obtém uma sessão
     *
     * @param cfgXml Arquivo de configuração
     * @return Sessão aberta
     */
    public static Session getSession(String cfgXml) {
        if (cfgXml == null || cfgXml.isEmpty()) {
            throw new IllegalParametersException("Missing hibernate configuration file param.");
        }

        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure();
            configuration.configure(cfgXml);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory.openSession();
    }

    /**
     * Verifica se a fábrica de sessões está ativa
     *
     * @return true se sim / false se não
     */
    public static boolean isSessionFactoryActive() {
        return sessionFactory != null;
    }

    /**
     * Construtor
     */
    private HibernateUtil() {
    }
}
