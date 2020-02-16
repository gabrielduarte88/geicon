package com.geicon.blue.tasks.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * Utilitário do gerenciador de beans
 *
 * @author Gabriel Duarte
 */
public class BeanManagerUtil implements Serializable {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instância um bean
     *
     * @param <T> Classe do bean
     * @param beanManager Gerenciador de beans
     * @param type Classe do bean
     * @param scope Escopo
     * @return Nova instância
     */
    public static <T> T getBeanInstance(final BeanManager beanManager, final Class<T> type, final Class<? extends Annotation> scope) {
        final Context context = beanManager.getContext(scope);
        final Set<Bean<?>> beans = beanManager.getBeans(type);
        @SuppressWarnings("unchecked")
        final Bean<T> bean = (Bean<T>) beanManager.resolve(beans);
        final CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);

        return context.get(bean, creationalContext);
    }

    /**
     * Construtor
     */
    private BeanManagerUtil() {
    }
}
