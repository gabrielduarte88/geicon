package com.geicon.blue.framework.persistence.services;

import com.geicon.blue.framework.persistence.models.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação genérica de Service
 *
 * @author Gabriel Duarte
 * @param <T> Classe gerenciada
 */
public abstract class GenericService<T extends Entity> implements Service<T> {
    @Override
    public Criteria createCriteria() {
        return getDao().createCriteria();
    }

    @Override
    public Criteria parseFilter(Criteria criteria, String filterType, String filterField, String filterValue) throws ParseException {
        if (filterField != null && !filterField.isEmpty() && filterValue != null && !filterValue.isEmpty()) {
            switch (filterType) {
                case "integer": {
                    criteria.add(Restrictions.eq(filterField, Integer.parseInt(filterValue)));
                    break;
                }
                case "string": {
                    criteria.add(Restrictions.like(filterField, "%" + filterValue + "%"));
                    break;
                }
                case "boolean": {
                    if ("sim".equalsIgnoreCase(filterValue)) {
                        criteria.add(Restrictions.eq(filterField, true));
                    }
                    else if ("não".equalsIgnoreCase(filterValue)) {
                        criteria.add(Restrictions.eq(filterField, false));
                    }

                    break;
                }
                case "date": {
                    Date data = new SimpleDateFormat("dd/MM/yyyy").parse(filterValue);

                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(data);
                    cal1.set(Calendar.HOUR, 0);
                    cal1.set(Calendar.MINUTE, 0);
                    cal1.set(Calendar.SECOND, 0);

                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(cal1.getTime());
                    cal2.add(Calendar.DAY_OF_MONTH, 1);

                    criteria.add(Restrictions.between(filterField, cal1.getTime(), cal2.getTime()));

                    break;
                }
            }
        }

        return criteria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load(Criteria criteria) {
        return getDao().load(criteria);
    }

    @Override
    public Set<T> list(Criteria criteria) {
        return getDao().list(criteria);
    }

    @Override
    public Set<T> list(Criteria criteria, int first, int step) {
        return getDao().list(criteria, first, step);
    }

    @Override
    public int count(Criteria criteria) {
        return getDao().count(criteria);
    }

    @Override
    public Integer insert(T entity) throws PersistenceException {
        return getDao().insert(entity);
    }

    @Override
    public void update(T entity) throws PersistenceException {
        getDao().update(entity);
    }

    @Override
    public void delete(T entity) throws PersistenceException {
        getDao().delete(entity);
    }
}
