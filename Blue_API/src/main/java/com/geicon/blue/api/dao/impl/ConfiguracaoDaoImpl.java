package com.geicon.blue.api.dao.impl;

import com.geicon.blue.api.dao.api.ConfiguracaoDao;
import com.geicon.blue.api.models.Configuracao;
import com.geicon.blue.framework.persistence.dao.GenericDao;
import javax.enterprise.context.RequestScoped;

/**
 * Implementação de ConfiguracaoDao
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ConfiguracaoDaoImpl extends GenericDao<Configuracao> implements ConfiguracaoDao {
    //
}
