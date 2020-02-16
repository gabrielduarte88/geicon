package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.EnvioEmailDao;
import com.geicon.blue.api.models.EnvioEmail;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.EnvioEmailService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
}
