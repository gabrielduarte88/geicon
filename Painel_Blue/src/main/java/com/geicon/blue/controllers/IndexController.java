package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.annotations.Login;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Interesse;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.auth.UserSession;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.Mail;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.InteresseService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.UsuarioService;
import com.geicon.blue.validation.InteresseValidator;
import java.io.Serializable;
import java.util.Date;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller da página principal
 *
 * @author Gabriel Duarte
 */
@Controller
public class IndexController implements Serializable {
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
     * HTTP Request
     */
    @Inject
    private HttpServletRequest request;
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
     * Serviços de interesse
     */
    @Inject
    private InteresseService interesseService;
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
     * Página de apresentação
     */
    public void index() {
        Long codigoInteresse = new Date().getTime();

        request.getSession().setAttribute("codigoInteresse", codigoInteresse.toString());

        result.include("codigoInteresse", codigoInteresse);
    }

    /**
     * Registro de interesse
     *
     * @param interesse Interesse
     * @param codigoInteresse Código de verificação
     */
    @Post("/registro-interesse")
    public void interesse(Interesse interesse, String codigoInteresse) {
        Message msg;

        Validator val = new InteresseValidator(interesse);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                String codigoInteresseRegistrado = (String) request.getSession().getAttribute("codigoInteresse");

                if (codigoInteresseRegistrado != null && codigoInteresseRegistrado.equals(codigoInteresse)) {
                    if (!interesseService.verificarInteresseDuplicado(interesse)) {
                        tx = interesseService.getDao().getSession().beginTransaction();

                        interesseService.inserirInteresse(interesse);

                        tx.commit();

                        msg = new SuccessMessage("Obrigado por registrar o seu interesse!");
                    }
                    else {
                        msg = new ErrorMessage("Seu interesse está registrado em nossa base. Em breve entraremos em contato.");
                    }
                }
                else {
                    msg = new ErrorMessage("Código de interesse inválido.");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção do interesse", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Página principal
     */
    @Path("/")
    @Login
    public void main() {
    }

    /**
     * Login
     *
     * @param usuario Usuário
     */
    @Post("/login")
    @Login
    public void login(Usuario usuario) {
        Message msg;

        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty() || usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            msg = new ErrorMessage("Informe o e-mail e a senha para realizar o acesso.");
        }
        else {
            Instituicao instituicao = (Instituicao) result.included().get("instituicao");

            usuario = usuarioService.autenticarUsuario(instituicao, usuario);

            if (usuario == null) {
                msg = new ErrorMessage("Usuário e/ou senha inválidos.");
            }
            else {
                logService.log(instituicao, "Acesso ao sistema realizado");

                userSession.setUsuario(usuario.getId());
                msg = new SuccessMessage("");
            }
        }

        result.use(json()).from(msg, "msg").serialize();
    }

    /**
     * Logout
     */
    @Path("/logout")
    public void logout() {
        Instituicao instituicao = (Instituicao) result.included().get("instituicao");

        logService.log(instituicao, "Logout efetuado");

        request.getSession().invalidate();
        result.redirectTo(this.getClass()).main();
    }

    /**
     * Recuperação de senha
     *
     * @param usuario Usuário
     */
    @Post("/recuperar-senha")
    @Login
    public void recuperarSenha(Usuario usuario) {
        Message msg;

        Transaction tx = null;
        try {
            if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty() || usuario.getCelular() == null || usuario.getCelular().isEmpty()) {
                msg = new ErrorMessage("Informe o e-mail e o celular para recuperar sua senha.");
            }
            else {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                usuario = usuarioService.autenticarUsuarioPorEmailECelular(instituicao, usuario);

                if (usuario == null) {
                    msg = new ErrorMessage("Usuário não encontrado.");
                }
                else {
                    tx = usuarioService.getDao().getSession().beginTransaction();

                    String senha = usuarioService.gerarNovaSenhaUsuario(usuario);

                    StringBuilder sb = new StringBuilder(0);
                    sb.append("Olá ").append(usuario.getNome()).append(",<br /><br />");
                    sb.append("Sua nova senha de acesso ao Blue GEICon é:<br />");
                    sb.append(senha);
                    sb.append("<br /><br />");
                    sb.append("Atenciosamente,<br />");
                    sb.append("Equipe Blue GEICon");

                    mail.send(usuario.getEmail(), null, null, "Recuperação de senha", sb.toString());

                    msg = new SuccessMessage("Uma nova senha foi gerada e enviada no seu e-mail.");

                    tx.commit();

                    logService.log(instituicao, "Solicitação de recuperação de senha realizada");
                }
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e não foi possível enviar a nova senha. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de enviar uma nova senha ao usuário", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("type").serialize();
    }
}
