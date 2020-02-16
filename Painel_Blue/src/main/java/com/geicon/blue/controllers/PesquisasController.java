package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.AcaoService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.PesquisaService;
import com.geicon.blue.services.api.ResponsabilidadeService;
import com.geicon.blue.services.api.UsuarioService;
import com.geicon.blue.validation.PesquisaValidator;
import java.io.Serializable;
import java.text.ParseException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de pesquisas
 *
 * @author Gabriel Duarte
 */
@Controller
public class PesquisasController implements Serializable {
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
     * HTTP Request
     */
    @Inject
    private HttpServletRequest request;
    /**
     * Result
     */
    @Inject
    private Result result;
    /**
     * Serviços de pesquisas
     */
    @Inject
    private PesquisaService pesquisaService;
    /**
     * Serviços de usuários
     */
    @Inject
    private UsuarioService usuarioService;
    /**
     * Serviços de ações
     */
    @Inject
    private AcaoService acaoService;
    /**
     * Serviços de responsabilidade
     */
    @Inject
    private ResponsabilidadeService responsabilidadeService;
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
    @Get("/pesquisas")
    public void main() {
    }

    /**
     * Listar pesquisas
     *
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/pesquisas")
    public void listar(String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Instituicao instituicao = (Instituicao) result.included().get("instituicao");

        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");

        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<Pesquisa> searchResult = pesquisaService.listarPesquisas(instituicao, usuarioAtual, usuarioService.isAdministrador(usuarioAtual), filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("pesquisas", searchResult.getItems());
        result.include("total", searchResult.getTotal());
    }

    /**
     * Novo pesquisa
     */
    @Get
    @Path(value = "/pesquisas/novo", priority = Path.HIGH)
    public void novo() {
        Instituicao instituicao = (Instituicao) result.included().get("instituicao");

        result.include("usuarios", usuarioService.listarUsuariosOrdenadosPorNome(instituicao));
    }

    /**
     * Cadastrar pesquisa
     *
     * @param pesquisa Novo pesquisa
     */
    @Post("/pesquisas/novo")
    public void inserir(Pesquisa pesquisa) {
        Message msg;

        Validator val = new PesquisaValidator(pesquisa, true);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                tx = pesquisaService.getDao().getSession().beginTransaction();

                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                pesquisaService.inserirPesquisa(instituicao, pesquisa);

                tx.commit();

                logService.log(instituicao, "Pesquisa #%d inserida", pesquisa.getId());

                msg = new SuccessMessage("Pesquisa inserida com sucesso!", pesquisa.getCode());
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de pesquisa", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar pesquisa
     *
     * @param pesquisa Pesquisa visualizado
     */
    @Get
    @Path(value = "/pesquisas/{pesquisa.code}", priority = Path.LOW)
    public void visualizar(Pesquisa pesquisa) {
        if (pesquisa != null && pesquisa.getId() != null) {
            Instituicao instituicao = (Instituicao) result.included().get("instituicao");

            pesquisa = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

            if (pesquisa != null) {
                result.include("pesquisa", pesquisa);
                result.include("usuarios", usuarioService.listarUsuariosOrdenadosPorNome(instituicao));
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
     * Alterar pesquisa
     *
     * @param pesquisa Pesquisa alterado
     */
    @Post("/pesquisas/{pesquisa.code}/alterar")
    public void alterar(Pesquisa pesquisa) {
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");

        Message msg;

        Validator val = new PesquisaValidator(pesquisa, usuarioService.isAdministrador(usuarioAtual));

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                if (pesquisa.getId() != null) {
                    Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                    Pesquisa pesquisaAt = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

                    pesquisaAt.setNome(pesquisa.getNome());

                    if (usuarioService.isAdministrador(usuarioAtual)) {
                        pesquisaAt.setResponsavel(pesquisa.getResponsavel());
                    }

                    tx = pesquisaService.getDao().getSession().beginTransaction();

                    pesquisaService.update(pesquisaAt);

                    tx.commit();

                    logService.log(instituicao, "Pesquisa #%d alterada", pesquisaAt.getId());

                    msg = new SuccessMessage("Pesquisa alterada com sucesso!");
                }
                else {
                    msg = new ErrorMessage("Os dados necessários não foram enviados");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de pesquisa", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Remover pesquisa
     *
     * @param pesquisa Pesquisa excluido
     */
    @Post("/pesquisas/{pesquisa.code}/remover")
    public void excluir(Pesquisa pesquisa) {
        Message msg;

        Transaction tx = null;

        try {
            if (pesquisa.getId() != null) {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                Pesquisa pesquisaAt = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

                tx = pesquisaService.getDao().getSession().beginTransaction();

                pesquisaService.excluirPesquisa(pesquisaAt);

                tx.commit();

                logService.log(instituicao, "Pesquisa #%d removida", pesquisaAt.getId());

                msg = new SuccessMessage("Pesquisa removida com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de pesquisa", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar participantes da pesquisa
     *
     * @param pesquisa Pesquisa visualizado
     */
    @Get("/pesquisas/{pesquisa.code}/participantes")
    public void participantes(Pesquisa pesquisa) {
        try {
            if (pesquisa != null && pesquisa.getId() != null) {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                pesquisa = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

                if (pesquisa != null) {
                    result.include("pesquisa", pesquisa);
                    result.include("usuarios", usuarioService.listarUsuariosOrdenadosPorNome(instituicao));
                    result.include("acoes", acaoService.listarAcoes());

                    result.include("responsabilidadeService", responsabilidadeService);

                }
                else {
                    result.nothing();
                }
            }
            else {
                result.nothing();
            }
        }
        catch (Exception ex) {
            logger.error("Houve um erro na visualização da pesquisa", ex);
            result.nothing();
        }
    }

    /**
     * Salvar participantes
     *
     * @param pesquisa Pesquisa alterada
     */
    @Post("/pesquisas/{pesquisa.code}/participantes")
    public void salvarParticipantes(Pesquisa pesquisa) {
        Message msg;

        Transaction tx = null;

        try {
            if (pesquisa.getId() != null) {
                Instituicao instituicao = (Instituicao) result.included().get("instituicao");

                pesquisa = pesquisaService.buscarPesquisaPorId(instituicao, pesquisa.getId());

                tx = pesquisaService.getDao().getSession().beginTransaction();

                for (Usuario usuario : usuarioService.listarUsuariosOrdenadosPorNome(instituicao)) {
                    String acoesId = request.getParameter("participante" + usuario.getId());

                    if (acoesId != null) {
                        responsabilidadeService.atualizarResponsabilidades(pesquisa, usuario, acaoService, acoesId.split(","));
                    }
                }

                tx.commit();

                logService.log(instituicao, "Participantes alterados na pesquisa #%d", pesquisa.getId());

                msg = new SuccessMessage("Participantes salvos com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de alteração dos participantes da pesquisa", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }
}
