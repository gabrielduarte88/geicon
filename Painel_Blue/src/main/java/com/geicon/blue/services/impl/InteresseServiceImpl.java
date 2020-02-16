package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.InteresseDao;
import com.geicon.blue.api.models.Interesse;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.InteresseService;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de InteresseService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class InteresseServiceImpl extends GenericService<Interesse> implements InteresseService {
    /**
     * DAO
     */
    @Inject
    protected InteresseDao dao;

    @Override
    public InteresseDao getDao() {
        return dao;
    }

    @Override
    public void inserirInteresse(Interesse interesse) {
        interesse.setData(new Date());

        insert(interesse);
    }

    @Override
    public boolean verificarInteresseDuplicado(Interesse interesse) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("email", interesse.getEmail()));

        return count(c) > 0;
    }

}
