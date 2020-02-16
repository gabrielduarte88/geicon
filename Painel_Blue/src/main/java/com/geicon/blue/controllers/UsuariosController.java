package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.Mail;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.util.Security;
import com.geicon.blue.framework.util.Strings;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.UsuarioService;
import com.geicon.blue.validation.UsuarioValidator;
import java.io.Serializable;
import java.text.ParseException;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de usuários
 *
 * @author Gabriel Duarte
 */
@Controller
public class UsuariosController implements Serializable {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * Ambiente
     */
    @Inject
    private Environment environment;
    /**
     * Mailer
     */
    @Inject
    private Mail mail;
    /**
     * Result
     */
    @Inject
    private Result result;
    /**
     * Serviços de usuários
     */
    @Inject
    private UsuarioService usuarioService;
    /**
     * Serviços de log
     */
    @Inject
    private LogService logService;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Página principal
     */
    @Get("/usuarios")
    public void main() {
    }

    /**
     * Listar usuários
     *
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/usuarios")
    public void listar(String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Instituicao instituicao = (Instituicao) result.included().get("instituicao");

        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<Usuario> searchResult = usuarioService.listarUsuarios(instituicao, filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("usuarios", searchResult.getItems());
        result.include("total", searchResult.getTotal());
    }

    /**
     * Novo usuário
     */
    @Get
    @Path(value = "/usuarios/novo", priority = Path.HIGH)
    public void novo() {
    }

    /**
     * Cadastrar usuário
     *
     * @param usuario Novo usuário
     */
    @Post("/usuarios/novo")
    public void inserir(Usuario usuario) {
        Message msg;

        Validator val = new UsuarioValidator(usuario);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                if (!usuarioService.verificarUsuarioDuplicado(instituicao, usuario, false)) {
                    String senha = Strings.createPassword();

                    usuario.setSenha(Security.hash(senha));

                    tx = usuarioService.getDao().getSession().beginTransaction();

                    usuarioService.inserirUsuario(instituicao, usuario);

                    StringBuilder sb = new StringBuilder(0);
                    sb.append("Olá ").append(usuario.getNome()).append(",<br /><br />");
                    sb.append("Sua nova senha de acesso ao Blue GEICon é:<br />");
                    sb.append(senha);
                    sb.append("<br /><br />");
                    sb.append("Atenciosamente,<br />");
                    sb.append("Equipe Blue GEICon");

                    mail.send(usuario.getEmail(), null, null, "Senha de acesso", sb.toString());

                    tx.commit();

                    logService.log(instituicao, "Usuário #%d inserido", usuario.getId());

                    msg = new SuccessMessage("Usuário inserido com sucesso!", usuario.getCode());
                }
                else {
                    msg = new ErrorMessage("Alguns erros foram encontrados:", "Já existe um usuário com o e-mail informado.");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de usuário", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar usuário
     *
     * @param usuario Usuario visualizado
     */
    @Get
    @Path(value = "/usuarios/{usuario.code}", priority = Path.LOW)
    public void visualizar(Usuario usuario) {
        if (usuario != null && usuario.getId() != null) {
            Instituicao instituicao = (Instituicao) result.included().get("instituicao");

            usuario = usuarioService.buscarUsuarioPorId(instituicao, usuario.getId());

            if (usuario != null) {
                result.include("usuario", usuario);
            }
            else {
                result.redirectTo(this).main();
            }
        }
        else {
            result.redirectTo(this).main();
        }
    }

    /**
     * Alterar usuário
     *
     * @param usuario Usuário alterado
     */
    @Post("/usuarios/{usuario.code}/alterar")
    public void alterar(Usuario usuario) {
        Message msg;

        Validator val = new UsuarioValidator(usuario);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                if (usuario.getId() != null) {
                    Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                    if (!usuarioService.verificarUsuarioDuplicado(instituicao, usuario, true)) {
                        Usuario usuarioAt = usuarioService.buscarUsuarioPorId(instituicao, usuario.getId());

                        usuarioAt.setNome(usuario.getNome());
                        usuarioAt.setEmail(usuario.getEmail());
                        usuarioAt.setCelular(usuario.getCelular());
                        usuarioAt.setAdministrador(usuario.getAdministrador());

                        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                            usuarioAt.setSenha(Security.hash(usuario.getSenha()));
                        }

                        tx = usuarioService.getDao().getSession().beginTransaction();

                        usuarioService.update(usuarioAt);

                        tx.commit();

                        logService.log(instituicao, "Usuário #%d alterado", usuarioAt.getId());

                        msg = new SuccessMessage("Usuário alterado com sucesso!");
                    }
                    else {
                        msg = new ErrorMessage("Alguns erros foram encontrados:", "Já existe um usuário com o e-mail informado.");
                    }
                }
                else {
                    msg = new ErrorMessage("Os dados necessários não foram enviados");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de usuário", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Remover usuário
     *
     * @param usuario Usuario excluido
     */
    @Post("/usuarios/{usuario.code}/remover")
    public void excluir(Usuario usuario) {
        Message msg;

        Transaction tx = null;

        try {
            if (usuario.getId() != null) {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                Usuario usuarioAt = usuarioService.buscarUsuarioPorId(instituicao, usuario.getId());

                tx = usuarioService.getDao().getSession().beginTransaction();

                usuarioService.excluirUsuario(usuarioAt);

                tx.commit();

                logService.log(instituicao, "Usuário #%d removido", usuarioAt.getId());

                msg = new SuccessMessage("Usuário removido com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de usuário", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }
}
