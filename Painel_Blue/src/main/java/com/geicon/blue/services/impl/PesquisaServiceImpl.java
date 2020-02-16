package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.PesquisaDao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.services.api.PesquisaService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 * Implementação de PesquisaService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class PesquisaServiceImpl extends GenericService<Pesquisa> implements PesquisaService {
    /**
     * DAO
     */
    @Inject
    protected PesquisaDao dao;

    @Override
    public PesquisaDao getDao() {
        return dao;
    }

    @Override
    public Pesquisa buscarPesquisaPorId(Instituicao instituicao, Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public Set<Pesquisa> listarPesquisasPorUsuario(Instituicao instituicao, Usuario usuario) {
        Criteria c = createCriteria();

        c.createAlias("responsabilidades", "responsabilidade", JoinType.LEFT_OUTER_JOIN);

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.or(
                Restrictions.eq("responsavel", usuario),
                Restrictions.and(
                        Restrictions.eq("responsabilidade.usuario", usuario),
                        Restrictions.isNull("responsabilidade.excluido")
                )
        ));
        c.add(Restrictions.isNull("excluido"));

        c.addOrder(Order.asc("nome"));

        return list(c);
    }

    @Override
    public boolean isParticipante(Instituicao instituicao, Pesquisa pesquisa, Usuario usuario) {
        Criteria c = createCriteria();

        c.createAlias("responsabilidades", "responsabilidade", JoinType.LEFT_OUTER_JOIN);

        c.add(Restrictions.eq("id", pesquisa.getId()));
        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.or(
                Restrictions.eq("responsavel", usuario),
                Restrictions.and(
                        Restrictions.eq("responsabilidade.usuario", usuario),
                        Restrictions.isNull("responsabilidade.excluido")
                )
        ));
        c.add(Restrictions.isNull("excluido"));

        return count(c) > 0;
    }

    @Override
    public boolean possuiResponsabilidade(Instituicao instituicao, Pesquisa pesquisa, Usuario usuario, String nomeAcao) {
        Criteria c = createCriteria();

        c.createAlias("responsabilidades", "responsabilidade");
        c.createAlias("responsabilidade.acao", "acao");

        c.add(Restrictions.eq("id", pesquisa.getId()));
        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("responsabilidade.usuario", usuario));
        c.add(Restrictions.eq("acao.nome", nomeAcao));
        c.add(Restrictions.isNull("responsabilidade.excluido"));
        c.add(Restrictions.isNull("excluido"));

        return count(c) > 0;
    }

    @Override
    public int contarPesquisasPorUsuarioResponsavel(Instituicao instituicao, Usuario responsavel) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("responsavel", responsavel));
        c.add(Restrictions.isNull("excluido"));

        return count(c);
    }

    @Override
    public boolean isResponsavelPesquisas(Instituicao instituicao, Usuario usuario) {
        return contarPesquisasPorUsuarioResponsavel(instituicao, usuario) > 0;
    }

    @Override
    public SearchResult<Pesquisa> listarPesquisas(Instituicao instituicao, Usuario usuarioAtual, boolean admin, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c.add(Restrictions.eq("instituicao", instituicao));
            cCount.add(Restrictions.eq("instituicao", instituicao));

            c.createAlias("responsavel", "responsavel");
            cCount.createAlias("responsavel", "responsavel");

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            if (!admin) {
                c.add(Restrictions.eq("responsavel", usuarioAtual));
                cCount.add(Restrictions.eq("responsavel", usuarioAtual));
            }

            c.add(Restrictions.isNull("excluido"));
            cCount.add(Restrictions.isNull("excluido"));

            if (order.equals("ASC")) {
                c.addOrder(Order.asc(orderField));
            }
            else {
                c.addOrder(Order.desc(orderField));
            }

            c.setFirstResult(page * itemsPerPage);
            c.setMaxResults(itemsPerPage);

            return new SearchResult<>(list(c), count(cCount));
        }
        catch (Exception ex) {
            throw new InternalException(ex);
        }
    }

    @Override
    public void inserirPesquisa(Instituicao instituicao, Pesquisa pesquisa) {
        pesquisa.setInstituicao(instituicao);
        pesquisa.setData(new Date());

        insert(pesquisa);
    }

    @Override
    public void excluirPesquisa(Pesquisa pesquisa) {
        pesquisa.setExcluido(new Date());

        update(pesquisa);
    }
}
