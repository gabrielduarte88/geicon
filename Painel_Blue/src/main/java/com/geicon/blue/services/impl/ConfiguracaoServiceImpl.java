package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.ConfiguracaoDao;
import com.geicon.blue.api.models.Configuracao;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.ConfiguracaoService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Implementação de ConfiguracaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ConfiguracaoServiceImpl extends GenericService<Configuracao> implements ConfiguracaoService {
    /**
     * DAO
     */
    @Inject
    protected ConfiguracaoDao dao;

    @Override
    public ConfiguracaoDao getDao() {
        return dao;
    }
}
