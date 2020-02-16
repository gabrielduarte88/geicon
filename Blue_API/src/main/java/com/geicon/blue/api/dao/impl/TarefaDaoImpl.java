package com.geicon.blue.api.dao.impl;

import com.geicon.blue.api.dao.api.TarefaDao;
import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.framework.persistence.dao.GenericDao;
import javax.enterprise.context.RequestScoped;

/**
 * Implementação de TarefaDao
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class TarefaDaoImpl extends GenericDao<Tarefa> implements TarefaDao {
    //
}
