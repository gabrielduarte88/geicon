package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.InstituicaoDao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.InstituicaoService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de InstituicaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class InstituicaoServiceImpl extends GenericService<Instituicao> implements InstituicaoService {
    /**
     * DAO
     */
    @Inject
    protected InstituicaoDao dao;

    @Override
    public InstituicaoDao getDao() {
        return dao;
    }

    @Override
    public Instituicao buscarInstituicaoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));

        return load(c);
    }

    @Override
    public Instituicao buscarInstituicaoPorDominio(String dominio) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("dominio", dominio));

        return load(c);
    }

}
