package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.auth.UserSession;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.Security;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.PesquisaService;
import com.geicon.blue.services.api.UsuarioService;
import com.geicon.blue.validation.UsuarioValidator;
import java.io.Serializable;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller da dashboard
 *
 * @author Gabriel Duarte
 */
@Controller
public class DashboardController implements Serializable {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * Sessão do usuário
     */
    @Inject
    private UserSession userSession;
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
     * Serviços de pesquisa
     */
    @Inject
    private PesquisaService pesquisaService;
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
     * DASHBOARD
     */
    @Path("/dashboard")
    public void main() {
    }

    /**
     * Menu
     */
    @Path("/menu")
    public void menu() {
        result.forwardTo("/WEB-INF/jsp/shared/menu.jsp");
    }

    /**
     * Dados pessoais
     */
    @Get("/dados-pessoais")
    public void dadosPessoais() {
    }

    /**
     * Dados pessoais - Alterar
     *
     * @param usuario Usuário
     */
    @Post("/dados-pessoais")
    public void dadosPessoaisAlterar(Usuario usuario) {
        Message msg;

        Validator val = new UsuarioValidator(usuario);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                Usuario usuarioAt = (Usuario) result.included().get("usuarioAtual");

                if (usuarioAt != null) {
                    usuarioAt.setNome(usuario.getNome());
                    usuarioAt.setEmail(usuario.getEmail());
                    usuarioAt.setCelular(usuario.getCelular());

                    if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                        usuarioAt.setSenha(Security.hash(usuario.getSenha()));
                    }

                    Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                    if (!usuarioService.verificarUsuarioDuplicado(instituicao, usuarioAt, true)) {
                        tx = usuarioService.getDao().getSession().beginTransaction();

                        usuarioService.update(usuarioAt);

                        tx.commit();

                        logService.log((Instituicao) result.included().get("instituicao"), "Dados pessoais alterados");

                        msg = new SuccessMessage("Dados alterados com sucesso!");
                    }
                    else {
                        msg = new ErrorMessage("Alguns erros foram encontrados:", "Já existe um usuário com o e-mail informado.");
                    }
                }
                else {
                    msg = new ErrorMessage("O usuário informado não corresponde ao usuário autenticado.");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de dados pessoais", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Configurações
     */
    @Get("/configuracoes")
    public void configuracoes() {
    }

    /**
     * Selecionar pesquisa
     *
     * @param pesquisa Pesquisa selecionada
     */
    @Get("/selecionar-pesquisa")
    public void selecionarPesquisa(Pesquisa pesquisa) {
        Message msg;

        try {
            Usuario usuarioAt = (Usuario) result.included().get("usuarioAtual");

            if (usuarioAt != null) {
                if (pesquisa != null && pesquisa.getId() != null) {
                    Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                    pesquisa = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

                    if (pesquisa != null) {
                        if (pesquisaService.isParticipante(instituicao, pesquisa, usuarioAt)) {
                            userSession.setPesquisa(pesquisa.getId());

                            msg = new SuccessMessage(pesquisa.getNome());
                        }
                        else {
                            msg = new ErrorMessage("O usuário não possui permissões na pesquisa informada.");
                        }
                    }
                    else {
                        msg = new ErrorMessage("A pesquisa informada não existe.");
                    }
                }
                else {
                    msg = new SuccessMessage("");
                    userSession.setPesquisa(null);
                }
            }
            else {
                msg = new ErrorMessage("O usuário informado não corresponde ao usuário autenticado.");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a seleção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de seleção da pesquisa", ex);
        }

        result.use(json()).from(msg, "msg").serialize();
    }
}
