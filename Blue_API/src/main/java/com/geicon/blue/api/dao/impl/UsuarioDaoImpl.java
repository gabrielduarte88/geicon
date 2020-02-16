package com.geicon.blue.api.dao.impl;

import com.geicon.blue.framework.persistence.dao.GenericDao;
import com.geicon.blue.api.dao.api.UsuarioDao;
import com.geicon.blue.api.models.Usuario;
import javax.enterprise.context.RequestScoped;

/**
 * Implementação de UsuarioDao
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class UsuarioDaoImpl extends GenericDao<Usuario> implements UsuarioDao {
    //
}
