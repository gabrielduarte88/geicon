package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.ObjetoDao;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.ObjetoService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de ObjetoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ObjetoServiceImpl extends GenericService<Objeto> implements ObjetoService {
    /**
     * DAO
     */
    @Inject
    protected ObjetoDao dao;

    @Override
    public ObjetoDao getDao() {
        return dao;
    }

    @Override
    public Objeto buscarObjetoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public Objeto buscarObjetoPorPosicao(Elicitacao elicitacao, ObjetoOcorrencia ocorrencia) {
        Criteria c = createCriteria();

        c.createAlias("ocorrenciasObjeto", "ocorrencia");

        c.add(Restrictions.eq("elicitacao", elicitacao));
        c.add(Restrictions.eq("ocorrencia.documento", ocorrencia.getDocumento()));
        c.add(Restrictions.eq("ocorrencia.posicaoInicial", ocorrencia.getPosicaoInicial()));
        c.add(Restrictions.eq("ocorrencia.posicaoFinal", ocorrencia.getPosicaoFinal()));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public void inserirObjeto(Objeto objeto) {
        objeto.setData(new Date());

        insert(objeto);
    }

    @Override
    public void excluirObjeto(Objeto objeto) {
        objeto.setExcluido(new Date());

        update(objeto);
    }

    @Override
    public Set<Objeto> listarObjetosPorElicitacao(Elicitacao elicitacao) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("elicitacao", elicitacao));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }
}
