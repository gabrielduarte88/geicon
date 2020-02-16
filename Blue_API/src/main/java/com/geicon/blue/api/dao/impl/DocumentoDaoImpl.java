package com.geicon.blue.api.dao.impl;

import com.geicon.blue.framework.persistence.dao.GenericDao;
import com.geicon.blue.api.dao.api.DocumentoDao;
import com.geicon.blue.api.models.Documento;
import javax.enterprise.context.RequestScoped;

/**
 * Implementação de DocumentoDao
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class DocumentoDaoImpl extends GenericDao<Documento> implements DocumentoDao {
    //
}
