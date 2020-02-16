package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.AgenteDao;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.services.api.AgenteService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de AgenteService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class AgenteServiceImpl extends GenericService<Agente> implements AgenteService {
    /**
     * DAO
     */
    @Inject
    protected AgenteDao dao;

    @Override
    public AgenteDao getDao() {
        return dao;
    }

    @Override
    public Agente buscarAgentePorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public SearchResult<Agente> listarAgentes(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c.createAlias("base", "base");
            cCount.createAlias("base", "base");

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            c.add(Restrictions.eq("base.pesquisa", pesquisa));
            cCount.add(Restrictions.eq("base.pesquisa", pesquisa));

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
    public void inserirAgente(Agente agente) {
        agente.setData(new Date());

        insert(agente);
    }

    @Override
    public void excluirAgente(Agente agente) {
        agente.setExcluido(new Date());

        update(agente);
    }

    @Override
    public Set<Agente> listarAgentesPorBaseDeConhecimento(BaseConhecimento baseConhecimento) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("base", baseConhecimento));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }
}
