package com.geicon.blue.framework.persistence.dao;

import com.geicon.blue.framework.persistence.models.Entity;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * Implementação genérica de Dao
 *
 * @author Gabriel Duarte
 * @param <T> Classe gerenciada
 */
public abstract class GenericDao<T extends Entity> implements Dao<T> {
    /**
     * Hibernate session
     */
    @Inject
    private Session session;
    /**
     * Classe gerenciada
     */
    private final Class<T> persistentClass;

    /**
     * Construtor
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public GenericDao() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        else {
            Class<T> tmpPersistentClass = null;

            Class superClass = getClass().getSuperclass();

            while (!superClass.equals(GenericDao.class) || !superClass.equals(Object.class)) {
                try {
                    tmpPersistentClass = (Class<T>) ((ParameterizedType) superClass.getGenericSuperclass()).getActualTypeArguments()[0];
                    break;
                }
                catch (Exception ex) {
                    //
                }

                superClass = superClass.getSuperclass();
            }

            persistentClass = tmpPersistentClass;
        }
    }

    /**
     * Construtor
     *
     * @param session Hibernate session
     */
    @SuppressWarnings("unchecked")
    public GenericDao(Session session) {
        this();
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Criteria createCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object uniqueResult(Criteria criteria) {
        criteria.setMaxResults(1);

        return criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load(Criteria criteria) {
        criteria.setMaxResults(1);

        return (T) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> list() {
        return new LinkedHashSet<>(getSession().createCriteria(persistentClass).list());
    }

    @Override
    @SuppressWarnings({"unchecked", "unchecked"})
    public Set<T> list(Criteria criteria) {
        return new LinkedHashSet<>(criteria.list());
    }

    @Override
    @SuppressWarnings({"unchecked", "unchecked"})
    public Set<T> list(Criteria criteria, int first, int step) {
        criteria.setFirstResult(first);
        criteria.setMaxResults(step);

        return new LinkedHashSet<>(criteria.list());
    }

    @Override
    public int count() {
        return ((Long) (getSession().createCriteria(persistentClass).setProjection(Projections.rowCount()).list().get(0))).intValue();
    }

    @Override
    public int count(Criteria criteria) {
        return ((Number) (criteria.setProjection(Projections.rowCount()).list().get(0))).intValue();
    }

    @Override
    public Integer insert(Entity entity) throws PersistenceException {
        getSession().save(entity);

        return entity.getId();
    }

    @Override
    public void update(Entity entity) throws PersistenceException {
        getSession().merge(entity);
    }

    @Override
    public void delete(Entity entity) throws PersistenceException {
        getSession().delete(entity);
    }
}
