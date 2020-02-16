package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.UsuarioDao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.util.Security;
import com.geicon.blue.framework.util.Strings;
import com.geicon.blue.services.api.UsuarioService;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de UsuarioService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class UsuarioServiceImpl extends GenericService<Usuario> implements UsuarioService {
    /**
     * DAO
     */
    @Inject
    protected UsuarioDao dao;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    @Override
    public UsuarioDao getDao() {
        return dao;
    }

    @Override
    public Usuario buscarUsuarioPorId(Instituicao instituicao, Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public Usuario autenticarUsuario(Instituicao instituicao, Usuario usuario) {
        try {
            Criteria c = createCriteria();

            c.add(Restrictions.eq("instituicao", instituicao));
            c.add(Restrictions.eq("email", usuario.getEmail()));
            c.add(Restrictions.eq("senha", Security.hash(usuario.getSenha())));
            c.add(Restrictions.isNull("excluido"));

            return load(c);
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            logger.error("Houve um erro na autenticação do usuário", ex);

            return null;
        }
    }

    @Override
    public Usuario autenticarUsuarioPorEmailECelular(Instituicao instituicao, Usuario usuario) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("email", usuario.getEmail()));
        c.add(Restrictions.eq("celular", usuario.getCelular()));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public String gerarNovaSenhaUsuario(Usuario usuario) {
        try {
            String senha = Strings.createPassword();

            usuario.setSenha(Security.hash(senha));

            update(usuario);

            return senha;
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            logger.error("Houve um erro na alteração de senha do usuário", ex);

            return null;
        }
    }

    @Override
    public boolean verificarUsuarioDuplicado(Instituicao instituicao, Usuario usuario, boolean alteracao) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.eq("email", usuario.getEmail()));
        c.add(Restrictions.isNull("excluido"));

        if (alteracao) {
            c.add(Restrictions.ne("id", usuario.getId()));
        }

        return count(c) > 0;
    }

    @Override
    public boolean isAdministrador(Usuario usuario) {
        return usuario != null && usuario.getAdministrador() != null && usuario.getAdministrador();
    }

    @Override
    public SearchResult<Usuario> listarUsuarios(Instituicao instituicao, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            c.add(Restrictions.eq("instituicao", instituicao));
            cCount.add(Restrictions.eq("instituicao", instituicao));

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
    public void inserirUsuario(Instituicao instituicao, Usuario usuario) {
        usuario.setInstituicao(instituicao);
        usuario.setData(new Date());

        insert(usuario);
    }

    @Override
    public void excluirUsuario(Usuario usuario) {
        usuario.setExcluido(new Date());

        update(usuario);
    }

    @Override
    public Set<Usuario> listarUsuariosOrdenadosPorNome(Instituicao instituicao) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("instituicao", instituicao));
        c.add(Restrictions.isNull("excluido"));

        c.addOrder(Order.asc("nome"));

        return list(c);
    }
}
