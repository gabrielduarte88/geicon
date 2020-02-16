package com.geicon.blue.tasks.services.impl;

import com.geicon.blue.api.dao.api.TarefaDao;
import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.api.models.enums.TarefaStatus;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.tasks.services.api.TarefaService;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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

    @Override
    public int contarTarefasPendentes() {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("status", TarefaStatus.EM_ESPERA));
        c.add(Restrictions.or(
                Restrictions.isNull("dataIniExec"),
                Restrictions.le("dataIniExec", new Date())
        ));

        c.setProjection(Projections.count("id"));

        return ((Number) c.uniqueResult()).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Tarefa> listarTarefasPendentes() {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("status", TarefaStatus.EM_ESPERA));
        c.add(Restrictions.or(
                Restrictions.isNull("dataIniExec"),
                Restrictions.le("dataIniExec", new Date())
        ));

        return new LinkedHashSet<>(c.list());
    }

    @Override
    public Set<Tarefa> listarTarefasAntigas() {
        Criteria c = createCriteria();

        Calendar dataExclusao = new GregorianCalendar();
        dataExclusao.setTime(new Date());
        dataExclusao.add(Calendar.DAY_OF_MONTH, -30);

        c.add(Restrictions.or(
                Restrictions.eq("status", TarefaStatus.SUCESSO),
                Restrictions.eq("status", TarefaStatus.CANCELADA),
                Restrictions.eq("status", TarefaStatus.ERRO)
        ));
        c.add(Restrictions.or(
                Restrictions.le("dataFimExec", dataExclusao.getTime()),
                Restrictions.isNull("dataFimExec")
        ));

        return list(c);
    }

}
