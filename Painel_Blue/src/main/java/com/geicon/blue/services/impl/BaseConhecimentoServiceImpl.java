package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.BaseConhecimentoDao;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.services.api.BaseConhecimentoService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de BaseConhecimentoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class BaseConhecimentoServiceImpl extends GenericService<BaseConhecimento> implements BaseConhecimentoService {
    /**
     * DAO
     */
    @Inject
    protected BaseConhecimentoDao dao;

    @Override
    public BaseConhecimentoDao getDao() {
        return dao;
    }

    @Override
    public BaseConhecimento buscarBasePorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public SearchResult<BaseConhecimento> listarBases(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            c.add(Restrictions.eq("pesquisa", pesquisa));
            cCount.add(Restrictions.eq("pesquisa", pesquisa));

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
    public void inserirBaseConhecimento(BaseConhecimento baseConhecimento) {
        baseConhecimento.setData(new Date());

        insert(baseConhecimento);
    }

    @Override
    public void excluirBaseConhecimento(BaseConhecimento baseConhecimento) {
        baseConhecimento.setExcluido(new Date());

        update(baseConhecimento);
    }

    @Override
    public Set<BaseConhecimento> listarBasesPorPesquisa(Pesquisa pesquisa) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("pesquisa", pesquisa));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }
}
