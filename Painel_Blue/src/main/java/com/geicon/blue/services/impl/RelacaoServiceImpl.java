package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.RelacaoDao;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.RelacaoService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de RelacaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class RelacaoServiceImpl extends GenericService<Relacao> implements RelacaoService {
    /**
     * DAO
     */
    @Inject
    protected RelacaoDao dao;

    @Override
    public RelacaoDao getDao() {
        return dao;
    }

    @Override
    public Relacao buscarRelacaoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public boolean verificarRelacaoExistentePorPosicao(Relacao relacao) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("elicitacao", relacao.getElicitacao()));
        c.add(Restrictions.eq("documento", relacao.getDocumento()));
        c.add(Restrictions.eq("posicaoInicial", relacao.getPosicaoInicial()));
        c.add(Restrictions.eq("posicaoFinal", relacao.getPosicaoFinal()));
        c.add(Restrictions.isNull("excluido"));

        return count(c) > 0;
    }

    @Override
    public void inserirRelacao(Relacao relacao) {
        relacao.setData(new Date());

        insert(relacao);
    }

    @Override
    public void excluirRelacao(Relacao relacao) {
        relacao.setExcluido(new Date());

        update(relacao);
    }

    @Override
    public Set<Relacao> listarRelacoesPorElicitacao(Elicitacao elicitacao) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("elicitacao", elicitacao));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }

    @Override
    public int contarRelacoesPorDocumento(Documento documento) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("documento", documento));
        c.add(Restrictions.isNull("excluido"));

        return count(c);
    }

    @Override
    public Set<Relacao> buscarRelacoesPorOrigem(Objeto objeto) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("origem", objeto));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }

    @Override
    public Set<Relacao> buscarRelacoesPorDestino(Objeto objeto) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("destino", objeto));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }
}
