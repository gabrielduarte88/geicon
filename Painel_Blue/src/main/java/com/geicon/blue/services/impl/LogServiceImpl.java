package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.LogDao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Log;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.auth.UserSession;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.UsuarioService;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Transaction;

/**
 * Implementação de LogService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class LogServiceImpl extends GenericService<Log> implements LogService {
    /**
     * DAO
     */
    @Inject
    protected LogDao dao;
    /**
     * Sessão do usuário
     */
    @Inject
    private UserSession userSession;
    /**
     * Serviços de usuário
     */
    @Inject
    private UsuarioService usuarioService;

    @Override
    public LogDao getDao() {
        return dao;
    }

    @Override
    public void log(Instituicao instituicao, String registro, Object... dados) {
        if (userSession != null && userSession.getUsuario() != null) {
            Usuario usuario = usuarioService.buscarUsuarioPorId(instituicao, userSession.getUsuario());

            Transaction tx = getDao().getSession().beginTransaction();

            Log log = new Log();
            log.setUsuario(usuario);
            log.setRegistro(String.format(registro, dados));
            log.setData(new Date());

            insert(log);

            tx.commit();
        }
    }
}
