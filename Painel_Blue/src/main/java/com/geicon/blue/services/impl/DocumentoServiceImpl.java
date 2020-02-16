package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.DocumentoDao;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.services.api.DocumentoService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de DocumentoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class DocumentoServiceImpl extends GenericService<Documento> implements DocumentoService {
    /**
     * DAO
     */
    @Inject
    protected DocumentoDao dao;

    @Override
    public DocumentoDao getDao() {
        return dao;
    }

    @Override
    public Documento buscarDocumentoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public SearchResult<Documento> listarDocumentos(Agente agente, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            c.add(Restrictions.eq("agente", agente));
            cCount.add(Restrictions.eq("agente", agente));

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
    public void inserirDocumento(Documento documento) {
        documento.setData(new Date());

        insert(documento);
    }

    @Override
    public void excluirDocumento(Documento documento) {
        documento.setExcluido(new Date());

        update(documento);
    }

    @Override
    public int contarDocumentosPorAgente(Agente agente) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("agente", agente));
        c.add(Restrictions.isNull("excluido"));

        return count(c);
    }

    @Override
    public Set<Documento> listarDocumentosPorAgente(Agente agente) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("agente", agente));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }
}
