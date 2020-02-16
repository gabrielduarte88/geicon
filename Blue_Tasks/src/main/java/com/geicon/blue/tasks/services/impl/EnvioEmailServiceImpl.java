package com.geicon.blue.tasks.services.impl;

import com.geicon.blue.api.dao.api.EnvioEmailDao;
import com.geicon.blue.api.models.EnvioEmail;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.tasks.services.api.EnvioEmailService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de EnvioEmailService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class EnvioEmailServiceImpl extends GenericService<EnvioEmail> implements EnvioEmailService {
    /**
     * DAO
     */
    @Inject
    protected EnvioEmailDao dao;

    @Override
    public EnvioEmailDao getDao() {
        return dao;
    }

    @Override
    public EnvioEmail buscarEnvioPorId(int id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));

        return load(c);
    }

}
