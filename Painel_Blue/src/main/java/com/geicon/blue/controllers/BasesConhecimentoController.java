package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.BaseConhecimentoService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.validation.BaseConhecimentoValidator;
import java.io.Serializable;
import java.text.ParseException;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de bases de conhecimentos
 *
 * @author Gabriel Duarte
 */
@Controller
public class BasesConhecimentoController implements Serializable {
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
    @Get("/bases-conhecimento")
    public void main() {
    }

    /**
     * Listar bases de conhecimentos
     *
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/bases-conhecimento")
    public void listar(String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<BaseConhecimento> searchResult = baseConhecimentoService.listarBases(pesquisaAtual, filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("basesConhecimento", searchResult.getItems());
        result.include("total", searchResult.getTotal());
    }

    /**
     * Novo baseConhecimento
     */
    @Get
    @Path(value = "/bases-conhecimento/novo", priority = Path.HIGH)
    public void novo() {
    }

    /**
     * Cadastrar base de conhecimento
     *
     * @param baseConhecimento Nova base de conhecimento
     */
    @Post("/bases-conhecimento/novo")
    public void inserir(BaseConhecimento baseConhecimento) {
        Message msg;

        Validator val = new BaseConhecimentoValidator(baseConhecimento);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                Pesquisa pesquisa = (Pesquisa) result.included().get("pesquisaAtual");

                baseConhecimento.setPesquisa(pesquisa);

                tx = baseConhecimentoService.getDao().getSession().beginTransaction();

                baseConhecimentoService.inserirBaseConhecimento(baseConhecimento);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Base de conhecimento #%d inserida", baseConhecimento.getId());

                msg = new SuccessMessage("Base de conhecimento inserida com sucesso!", baseConhecimento.getCode());
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de base de conhecimento", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento visualizada
     */
    @Get
    @Path(value = "/bases-conhecimento/{baseConhecimento.code}", priority = Path.LOW)
    public void visualizar(BaseConhecimento baseConhecimento) {
        if (baseConhecimento != null && baseConhecimento.getId() != null) {
            baseConhecimento = baseConhecimentoService.buscarBasePorId(baseConhecimento.getId());

            if (baseConhecimento != null) {
                result.include("baseConhecimento", baseConhecimento);
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
     * Alterar base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento alterada
     */
    @Post("/bases-conhecimento/{baseConhecimento.code}/alterar")
    public void alterar(BaseConhecimento baseConhecimento) {
        Message msg;

        Validator val = new BaseConhecimentoValidator(baseConhecimento);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                if (baseConhecimento.getId() != null) {
                    BaseConhecimento baseConhecimentoAt = baseConhecimentoService.buscarBasePorId(baseConhecimento.getId());

                    baseConhecimentoAt.setTitulo(baseConhecimento.getTitulo());
                    baseConhecimentoAt.setProposicaoInicial(baseConhecimento.getProposicaoInicial());
                    baseConhecimentoAt.setDescricao(baseConhecimento.getDescricao());

                    tx = baseConhecimentoService.getDao().getSession().beginTransaction();

                    baseConhecimentoService.update(baseConhecimentoAt);

                    tx.commit();

                    logService.log((Instituicao) result.included().get("instituicao"), "Base de conhecimento #%d alterada", baseConhecimentoAt.getId());

                    msg = new SuccessMessage("Base de conhecimento alterada com sucesso!");
                }
                else {
                    msg = new ErrorMessage("Os dados necessários não foram enviados");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de base de conhecimento", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Remover base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento excluida
     */
    @Post("/bases-conhecimento/{baseConhecimento.code}/remover")
    public void excluir(BaseConhecimento baseConhecimento) {
        Message msg;

        Transaction tx = null;

        try {
            if (baseConhecimento.getId() != null) {
                BaseConhecimento baseConhecimentoAt = baseConhecimentoService.buscarBasePorId(baseConhecimento.getId());

                tx = baseConhecimentoService.getDao().getSession().beginTransaction();

                baseConhecimentoService.excluirBaseConhecimento(baseConhecimentoAt);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Base de conhecimento #%d removida", baseConhecimentoAt.getId());

                msg = new SuccessMessage("Base de conhecimento removida com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de base de conhecimento", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }
}
