package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.AgenteService;
import com.geicon.blue.services.api.BaseConhecimentoService;
import com.geicon.blue.services.api.DocumentoService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.validation.AgenteValidator;
import java.io.Serializable;
import java.text.ParseException;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de agentes
 *
 * @author Gabriel Duarte
 */
@Controller
public class AgentesController implements Serializable {
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
     * Result
     */
    @Inject
    private Result result;
    /**
     * Serviços de bases de conhecimento
     */
    @Inject
    private BaseConhecimentoService baseConhecimentoService;
    /**
     * Serviços de agentes
     */
    @Inject
    private AgenteService agenteService;
    /**
     * Serviços de agentes
     */
    @Inject
    private DocumentoService documentoService;
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
     *
     * @param baseConhecimento Base de conhecimento
     */
    @Get("/agentes")
    public void main(BaseConhecimento baseConhecimento) {
        result.include("baseConhecimento", baseConhecimentoService.buscarBasePorId(baseConhecimento.getId()));
    }

    /**
     * Listar agentes
     *
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/agentes")
    public void listar(String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<Agente> searchResult = agenteService.listarAgentes(pesquisaAtual, filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("agentes", searchResult.getItems());
        result.include("total", searchResult.getTotal());

        result.include("documentoService", documentoService);
    }

    /**
     * Novo agente
     */
    @Get
    @Path(value = "/agentes/novo", priority = Path.HIGH)
    public void novo() {
        Pesquisa pesquisa = (Pesquisa) result.included().get("pesquisaAtual");
        result.include("basesConhecimento", baseConhecimentoService.listarBasesPorPesquisa(pesquisa));
    }

    /**
     * Cadastrar agente
     *
     * @param agente Novo agente
     */
    @Post("/agentes/novo")
    public void inserir(Agente agente) {
        Message msg;

        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (agente != null && agente.getBase() != null) {
            agente.setBase(baseConhecimentoService.buscarBasePorId(agente.getBase().getId()));
        }

        Validator val = new AgenteValidator(agente, pesquisaAtual, false);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                tx = agenteService.getDao().getSession().beginTransaction();

                agenteService.inserirAgente(agente);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Agente #%d inserido", agente.getId());

                msg = new SuccessMessage("Agente inserido com sucesso!", agente.getCode());
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de agente", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar agente
     *
     * @param agente Agente visualizado
     */
    @Get
    @Path(value = "/agentes/{agente.code}", priority = Path.LOW)
    public void visualizar(Agente agente) {
        if (agente != null && agente.getId() != null) {
            agente = agenteService.buscarAgentePorId(agente.getId());

            if (agente != null) {
                result.include("agente", agente);
            }
            else {
                result.redirectTo(IndexController.class).main();
            }
        }
        else {
            result.redirectTo(IndexController.class).main();
        }
    }

    /**
     * Alterar agente
     *
     * @param agente Agente alterado
     */
    @Post("/agentes/{agente.code}/alterar")
    public void alterar(Agente agente) {
        Message msg;

        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        Validator val = new AgenteValidator(agente, pesquisaAtual, true);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                if (agente.getId() != null) {
                    Agente agenteAt = agenteService.buscarAgentePorId(agente.getId());

                    agenteAt.setNome(agente.getNome());
                    agenteAt.setDescricao(agente.getDescricao());

                    tx = agenteService.getDao().getSession().beginTransaction();

                    agenteService.update(agenteAt);

                    tx.commit();

                    logService.log((Instituicao) result.included().get("instituicao"), "Agente #%d alterado", agenteAt.getId());

                    msg = new SuccessMessage("Agente alterado com sucesso!");
                }
                else {
                    msg = new ErrorMessage("Os dados necessários não foram enviados");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de agente", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Remover agente
     *
     * @param agente Agente excluido
     */
    @Post("/agentes/{agente.code}/remover")
    public void excluir(Agente agente) {
        Message msg;

        Transaction tx = null;

        try {
            if (agente.getId() != null) {
                Agente agenteAt = agenteService.buscarAgentePorId(agente.getId());

                tx = agenteService.getDao().getSession().beginTransaction();

                agenteService.excluirAgente(agenteAt);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Agente #%d removido", agenteAt.getId());

                msg = new SuccessMessage("Agente removido com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de agente", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }
}
