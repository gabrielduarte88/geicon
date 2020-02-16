package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.TarefaDao;
import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.TarefaService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Implementação de TarefaService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class TarefaServiceImpl extends GenericService<Tarefa> implements TarefaService {
    /**
     * DAO
     */
    @Inject
    protected TarefaDao dao;

    @Override
    public TarefaDao getDao() {
        return dao;
    }
}
