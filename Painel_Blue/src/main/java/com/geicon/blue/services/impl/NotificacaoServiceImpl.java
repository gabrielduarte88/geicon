package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.NotificacaoDao;
import com.geicon.blue.api.models.Notificacao;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.NotificacaoService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Implementação de NotificacaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class NotificacaoServiceImpl extends GenericService<Notificacao> implements NotificacaoService {
    /**
     * DAO
     */
    @Inject
    protected NotificacaoDao dao;

    @Override
    public NotificacaoDao getDao() {
        return dao;
    }
}
