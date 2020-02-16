package com.geicon.blue.api.dao.impl;

import com.geicon.blue.api.dao.api.LogDao;
import com.geicon.blue.api.models.Log;
import com.geicon.blue.framework.persistence.dao.GenericDao;
import javax.enterprise.context.RequestScoped;

/**
 * Implementação de LogDao
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class LogDaoImpl extends GenericDao<Log> implements LogDao {
    //
}
