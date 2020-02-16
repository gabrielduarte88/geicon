package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.AcaoDao;
import com.geicon.blue.api.models.Acao;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.AcaoService;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de AcaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class AcaoServiceImpl extends GenericService<Acao> implements AcaoService {
    /**
     * DAO
     */
    @Inject
    protected AcaoDao dao;

    @Override
    public AcaoDao getDao() {
        return dao;
    }

    @Override
    public Set<Acao> listarAcoes() {
        Criteria c = createCriteria();

        return list(c);
    }

    @Override
    public Acao buscarAcaoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));

        return load(c);
    }
}
