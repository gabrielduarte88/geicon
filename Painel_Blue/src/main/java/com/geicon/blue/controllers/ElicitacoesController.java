package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.view.Results;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.api.models.enums.Controlabilidade;
import com.geicon.blue.api.models.enums.RelacaoPeso;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.AgenteService;
import com.geicon.blue.services.api.BaseConhecimentoService;
import com.geicon.blue.services.api.DocumentoService;
import com.geicon.blue.services.api.ElicitacaoService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.ObjetoOcorrenciaService;
import com.geicon.blue.services.api.ObjetoService;
import com.geicon.blue.services.api.RelacaoService;
import com.geicon.blue.validation.ElicitacaoValidator;
import com.geicon.blue.validation.ObjetoAtualizacaoValidator;
import com.geicon.blue.validation.ObjetoControlabilidadeAgenteValidator;
import com.geicon.blue.validation.ObjetoControlabilidadeDominioValidator;
import com.geicon.blue.validation.ObjetoValidator;
import com.geicon.blue.validation.RelacaoPesoValidator;
import com.geicon.blue.validation.RelacaoValidator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.text.ParseException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de elicitacaos
 *
 * @author Gabriel Duarte
 */
@Controller
public class ElicitacoesController implements Serializable {
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
     * HTTP Request
     */
    @Inject
    private HttpServletRequest request;
    /**
     * HTTP Response
     */
    @Inject
    private HttpServletResponse response;
    /**
     * Serviços de elicitações
     */
    @Inject
    private ElicitacaoService elicitacaoService;
    /**
     * Serviços de base de conhecimento
     */
    @Inject
    private BaseConhecimentoService baseConhecimentoService;
    /**
     * Serviços de agentes
     */
    @Inject
    private AgenteService agenteService;
    /**
     * Serviços de relação
     */
    @Inject
    private RelacaoService relacaoService;
    /**
     * Serviços de documento
     */
    @Inject
    private DocumentoService documentoService;
    /**
     * Serviços de objeto
     */
    @Inject
    private ObjetoService objetoService;
    /**
     * Serviços de ocorrências de objeto
     */
    @Inject
    private ObjetoOcorrenciaService objetoOcorrenciaService;
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
     *
     */
    @Get("/elicitacoes")
    public void main() {
    }

    /**
     * Listar elicitações
     *
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/elicitacoes")
    public void listar(String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<Elicitacao> searchResult = elicitacaoService.listarElicitacoes(pesquisaAtual, filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("elicitacoes", searchResult.getItems());
        result.include("total", searchResult.getTotal());
    }

    /**
     * Nova elicitação
     */
    @Get
    @Path(value = "/elicitacoes/novo", priority = Path.HIGH)
    public void novo() {
        Pesquisa pesquisa = (Pesquisa) result.included().get("pesquisaAtual");
        result.include("basesConhecimento", baseConhecimentoService.listarBasesPorPesquisa(pesquisa));
    }

    /**
     * Cadastrar elicitação
     *
     * @param elicitacao Nova elicitação
     */
    @Post("/elicitacoes/novo")
    public void inserir(Elicitacao elicitacao) {
        Message msg;

        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (elicitacao != null && elicitacao.getBase() != null) {
            elicitacao.setBase(baseConhecimentoService.buscarBasePorId(elicitacao.getBase().getId()));
        }
        if (elicitacao != null && elicitacao.getAgente() != null) {
            elicitacao.setAgente(agenteService.buscarAgentePorId(elicitacao.getAgente().getId()));
        }

        Validator val = new ElicitacaoValidator(elicitacao, pesquisaAtual);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                elicitacao.setResponsavel(usuarioAtual);

                tx = elicitacaoService.getDao().getSession().beginTransaction();

                elicitacaoService.inserirElicitacao(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Elicitação #%d inserida", elicitacao.getId());

                msg = new SuccessMessage("Elicitação inserida com sucesso!", elicitacao.getCode());
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de elicitação", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar elicitação
     *
     * @param elicitacao Elicitação visualizada
     */
    @Get
    @Path(value = "/elicitacoes/{elicitacao.code}", priority = Path.LOW)
    public void visualizar(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                result.include("elicitacao", elicitacao);
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
     * Remover elicitação
     *
     * @param elicitacao Elicitação excluida
     */
    @Post("/elicitacoes/{elicitacao.code}/remover")
    public void excluir(Elicitacao elicitacao) {
        Message msg;

        Transaction tx = null;

        try {
            if (elicitacao.getId() != null) {
                Elicitacao elicitacaoAt = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

                tx = elicitacaoService.getDao().getSession().beginTransaction();

                elicitacaoService.excluirElicitacao(elicitacaoAt);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Elicitação #%d removida", elicitacaoAt.getId());

                msg = new SuccessMessage("Elicitação removida com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de elicitação", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar grafo elicitação
     *
     * @param elicitacao Elicitação visualizada
     */
    @Get("/elicitacoes/{elicitacao.code}/grafo")
    public void grafo(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                result.include("elicitacao", elicitacao);

                String embedded = request.getParameter("embedded");

                if (embedded != null) {
                    result.include("embedded", true);
                }
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
     * Dados da elicitação para o grafo
     *
     * @param elicitacao Elicitação visualizada
     */
    @Get("/elicitacoes/{elicitacao.code}/dados")
    public void dados(Elicitacao elicitacao) {
        String res;

        try {
            //Geral
            StringBuilder sb = new StringBuilder(0);

            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            BaseConhecimento baseConhecimento = elicitacao.getBase();

            File file = new File(this.getClass().getClassLoader().getResource("data.json").toURI());

            BufferedReader br = new BufferedReader(new FileReader(file));

            String ln;
            while ((ln = br.readLine()) != null) {
                sb.append(ln);
            }

//            res = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
            res = sb.toString();

            res = res.replace("###PREPOSITION###", baseConhecimento.getProposicaoInicial());

            //Vértices
            sb = new StringBuilder(0);

            for (Objeto objeto : objetoService.listarObjetosPorElicitacao(elicitacao)) {
                Controlabilidade ca = objeto.getControlabilidadeAgente();
                Controlabilidade cd = objeto.getControlabilidadeDominio();

                if (ca != null && cd != null) {
                    int grupo = 0;

                    if (ca == Controlabilidade.NC) {
                        if (cd == Controlabilidade.CT) {
                            grupo = 9;
                        }
                        else if (cd == Controlabilidade.PN) {
                            grupo = 8;
                        }
                        else if (cd == Controlabilidade.NC) {
                            grupo = 7;
                        }
                    }
                    else if (ca == Controlabilidade.PN) {
                        if (cd == Controlabilidade.CT) {
                            grupo = 6;
                        }
                        else if (cd == Controlabilidade.PN) {
                            grupo = 5;
                        }
                        else if (cd == Controlabilidade.NC) {
                            grupo = 4;
                        }
                    }
                    else if (ca == Controlabilidade.CT) {
                        if (cd == Controlabilidade.CT) {
                            grupo = 3;
                        }
                        else if (cd == Controlabilidade.PN) {
                            grupo = 2;
                        }
                        else if (cd == Controlabilidade.NC) {
                            grupo = 1;
                        }
                    }

                    sb.append("{\"id\": ").append(objeto.getId()).append(",\"name\": \"").append(objeto.getNome()).append("\",\"group\": ").append(grupo).append("},");
                }
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            res = res.replace("###NODES###", sb.toString());

            //Arestas
            sb = new StringBuilder(0);

            for (Relacao relacao : relacaoService.listarRelacoesPorElicitacao(elicitacao)) {
                Objeto origem = relacao.getOrigem();
                Objeto destino = relacao.getDestino();

                if (origem != null && origem.getControlabilidadeAgente() != null && origem.getControlabilidadeDominio() != null
                        && destino != null && destino.getControlabilidadeAgente() != null && destino.getControlabilidadeDominio() != null
                        && relacao.getPeso() != null) {
                    sb.append("{\"id\": ").append(relacao.getId()).append(",\"name\": \"").append(relacao.getNome()).append("\",\"source\": ").append(origem.getId()).append(",\"target\": ").append(destino.getId()).append(",\"value\": 1,\"type\": \"").append(relacao.getPeso()).append("\"},");
                }
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            res = res.replace("###LINKS###", sb.toString());

            response.setHeader("Content-disposition", "inline; filename=json.json");
            response.setHeader("Content-Type", "application/json; charset=UTF-8;");
            response.getWriter().println(res);
        }
        catch (Exception ex) {
            logger.error("Não foi possível gerar os dados do grafo.", ex);
        }

        result.use(Results.nothing());
    }

    /**
     * Fluxograma
     *
     * @param elicitacao Elicitação visualizada
     */
    @Get("/elicitacoes/{elicitacao.code}/fluxograma")
    public void fluxograma(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                BaseConhecimento base = elicitacao.getBase();
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\n", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\t", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\r", " "));

                result.include("elicitacao", elicitacao);
                result.include("base", base);
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
     * Fluxograma
     *
     * @param elicitacao Elicitação visualizada
     */
    @Post("/elicitacoes/{elicitacao.code}/fluxograma")
    public void fluxogramaGrafar(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                BaseConhecimento base = elicitacao.getBase();
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\n", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\t", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\r", " "));

                result.include("elicitacao", elicitacao);
                result.include("base", base);

                //gravar
                result.forwardTo(this).fluxograma(elicitacao);
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
     * Elicitar
     *
     * @param elicitacao Elicitação visualizada
     */
    @Get("/elicitacoes/{elicitacao.code}/elicitar")
    public void elicitar(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                BaseConhecimento base = elicitacao.getBase();
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\n", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\t", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\r", " "));

                result.include("elicitacao", elicitacao);
                result.include("base", base);
                result.include("documentoService", documentoService);
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
     * Visualizar proposições da elicitação
     *
     * @param elicitacao Elicitação visualizada
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes")
    public void proposicoes(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                BaseConhecimento base = elicitacao.getBase();
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\n", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\t", " "));
                base.setProposicaoInicial(base.getProposicaoInicial().replace("\r", " "));

                result.include("elicitacao", elicitacao);
                result.include("base", base);
                result.include("relacaoService", relacaoService);
                result.include("editable", request.getParameter("editable"));
            }
        }
    }

    /**
     * Adiciona uma relação na elicitação
     *
     * @param elicitacao Elicitação visualizada
     * @param relacao Nova relação
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/adicionar")
    public void adicionarProposicao(Elicitacao elicitacao, Relacao relacao) {
        if (relacao != null && relacao.getDocumento() != null) {
            relacao.setDocumento(documentoService.buscarDocumentoPorId(relacao.getDocumento().getId()));
        }

        Validator val = new RelacaoValidator(relacao, false);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para a inserção da proposição"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            relacao.setElicitacao(elicitacao);

            if (!relacaoService.verificarRelacaoExistentePorPosicao(relacao)) {
                Transaction tx = null;
                try {
                    tx = relacaoService.getDao().getSession().beginTransaction();

                    relacaoService.inserirRelacao(relacao);

                    elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                    elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                    elicitacaoService.update(elicitacao);

                    tx.commit();

                    logService.log((Instituicao) result.included().get("instituicao"), "Relação #%d inserida na elicitação #%d", relacao.getId(), elicitacao.getId());
                }
                catch (Exception ex) {
                    result.include("msg", new ErrorMessage("Houve um erro na inserção da proposição."));
                    logger.error("Erro na tentativa de inserção de relação", ex);

                    if (tx != null) {
                        tx.rollback();
                    }
                }
            }
            else {
                result.include("msg", new ErrorMessage("A proposição cujo núcleo é o verbo '" + relacao.getNome() + "' já existe."));
            }
        }

        result.forwardTo(this).proposicoes(elicitacao);
    }

    /**
     * Alterar uma relação
     *
     * @param elicitacao Elicitação visualizada
     * @param relacao Relação removida
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/alterar")
    public void alterarProposicao(Elicitacao elicitacao, Relacao relacao) {
        Validator val = new RelacaoValidator(relacao, true);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para a alteração da proposição"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            String nome = relacao.getNome();

            relacao = relacaoService.buscarRelacaoPorId(relacao.getId());

            Transaction tx = null;
            try {
                tx = relacaoService.getDao().getSession().beginTransaction();

                relacao.setNome(nome);

                relacaoService.update(relacao);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Relação #%d alterada", relacao.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na alteração da proposição."));
                logger.error("Erro na tentativa de alteração de relação", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).proposicoes(elicitacao);
    }

    /**
     * Altear o peso de uma relação
     *
     * @param elicitacao Elicitação visualizada
     * @param relacao Relação removida
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/definirPeso")
    public void definirPesoProposicao(Elicitacao elicitacao, Relacao relacao) {
        Validator val = new RelacaoPesoValidator(relacao);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para a alteração da proposição"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            RelacaoPeso peso = relacao.getPeso();

            relacao = relacaoService.buscarRelacaoPorId(relacao.getId());

            Transaction tx = null;
            try {
                tx = relacaoService.getDao().getSession().beginTransaction();

                relacao.setPeso(peso);

                relacaoService.update(relacao);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Peso da relação #%d definido", relacao.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na alteração da proposição."));
                logger.error("Erro na tentativa de alteração de relação", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).proposicoes(elicitacao);
    }

    /**
     * Remover uma relação na elicitação
     *
     * @param elicitacao Elicitação visualizada
     * @param relacao Relação removida
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/remover")
    public void removerProposicao(Elicitacao elicitacao, Relacao relacao) {
        if (relacao == null || relacao.getId() == null) {
            result.include("msg", new ErrorMessage("Dados incompletos para a remoção da proposição"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            relacao = relacaoService.buscarRelacaoPorId(relacao.getId());

            Transaction tx = null;
            try {
                tx = relacaoService.getDao().getSession().beginTransaction();

                relacaoService.excluirRelacao(relacao);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Relação #%d removida", relacao.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na remoção da proposição."));
                logger.error("Erro na tentativa de remoção de relação", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).proposicoes(elicitacao);
    }

    /**
     * Visualizar conceitos da elicitação
     *
     * @param elicitacao Elicitação visualizada
     */
    @Post("/elicitacoes/{elicitacao.code}/conceitos")
    public void conceitos(Elicitacao elicitacao) {
        if (elicitacao != null && elicitacao.getId() != null) {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            if (elicitacao != null) {
                result.include("elicitacao", elicitacao);
                result.include("objetoService", objetoService);
                result.include("objetoOcorrenciaService", objetoOcorrenciaService);
            }
        }
    }

    /**
     * Adiciona um conceito na elicitação
     *
     * @param elicitacao Elicitação visualizada
     * @param relacao Relação alvo
     * @param objeto Novo objeto
     * @param ocorrencia Ocorrência do objeto
     * @param posicao Posição em relação a relação
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/adicionarConceito")
    public void adicionarConceito(Elicitacao elicitacao, Relacao relacao, Objeto objeto, ObjetoOcorrencia ocorrencia, String posicao) {
        relacao = relacaoService.buscarRelacaoPorId(relacao.getId());

        if (ocorrencia != null && ocorrencia.getDocumento() != null) {
            ocorrencia.setDocumento(documentoService.buscarDocumentoPorId(ocorrencia.getDocumento().getId()));
        }

        Validator val = new ObjetoValidator(relacao, objeto, ocorrencia, posicao);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para a inserção do conceito"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            objeto.setElicitacao(elicitacao);

            Transaction tx = null;
            try {
                tx = objetoService.getDao().getSession().beginTransaction();

                if (ocorrencia != null && ocorrencia.getDocumento() != null && ocorrencia.getPosicaoInicial() != null && ocorrencia.getPosicaoFinal() != null) {
                    Objeto objetoAnt = objetoService.buscarObjetoPorPosicao(elicitacao, ocorrencia);

                    if (objetoAnt == null) {
                        objeto.setElicitacao(elicitacao);

                        objetoService.inserirObjeto(objeto);
                    }
                    else {
                        objeto = objetoAnt;
                    }

                    if (posicao.equals("causa")) {
                        relacao.setOrigem(objeto);
                    }
                    else {
                        relacao.setDestino(objeto);
                    }

                    relacaoService.update(relacao);

                    if (objeto.getId() != null) {
                        if (!objetoOcorrenciaService.verificarOcorrenciaPorPosicao(objeto, ocorrencia)) {
                            ocorrencia.setObjeto(objeto);

                            objetoOcorrenciaService.inserirOcorrencia(ocorrencia);
                        }
                    }
                }
                else {
                    objeto.setElicitacao(elicitacao);

                    objetoService.inserirObjeto(objeto);

                    if (objeto.getId() != null) {
                        if (posicao.equals("causa")) {
                            relacao.setOrigem(objeto);
                        }
                        else {
                            relacao.setDestino(objeto);
                        }

                        relacaoService.update(relacao);
                    }
                }

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Conceito #%d adicionado em #%d", objeto.getId(), elicitacao.getId());

                objetoService.getDao().getSession().refresh(objeto);
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na inserção do conceito."));
                logger.error("Erro na tentativa de inserção de conceito", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).conceitos(elicitacao);
    }

    /**
     * Mesclar conceitos
     *
     * @param elicitacao Elicitação
     * @param objeto1 Objeto 1
     * @param objeto2 Objeto 2
     */
    @Post("/elicitacoes/{elicitacao.code}/proposicoes/mesclarConceitos")
    public void mesclarConceito(Elicitacao elicitacao, Objeto objeto1, Objeto objeto2) {
        if (elicitacao != null && elicitacao.getId() != null
                && objeto1 != null && objeto1.getId() != null
                && objeto2 != null && objeto2.getId() != null) {
            if (!objeto1.equals(objeto2)) {
                Transaction tx = null;
                try {
                    elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

                    tx = objetoService.getDao().getSession().beginTransaction();

                    objeto1 = objetoService.buscarObjetoPorId(objeto1.getId());
                    objeto2 = objetoService.buscarObjetoPorId(objeto2.getId());

                    for (ObjetoOcorrencia ocorrencia : objeto1.getOcorrenciasObjeto()) {
                        ocorrencia.setObjeto(objeto2);
                        objetoOcorrenciaService.update(ocorrencia);
                    }
                    objeto1.getOcorrenciasObjeto().clear();

                    for (Relacao relacao : objeto1.getRelacoesOrigem()) {
                        relacao.setOrigem(objeto2);
                        relacaoService.update(relacao);
                    }
                    objeto1.getRelacoesOrigem().clear();

                    for (Relacao relacao : objeto1.getRelacoesDestino()) {
                        relacao.setDestino(objeto2);
                        relacaoService.update(relacao);
                    }
                    objeto1.getRelacoesDestino().clear();

                    objetoService.update(objeto1);

                    objetoService.excluirObjeto(objeto1);

                    elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                    elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                    elicitacaoService.update(elicitacao);

                    tx.commit();

                    logService.log((Instituicao) result.included().get("instituicao"), "Conceitos #%d e #%d mesclados", objeto1.getId(), objeto2.getId());
                }
                catch (Exception ex) {
                    result.include("msg", new ErrorMessage("Houve um erro na mesclagem dos conceitos."));
                    logger.error("Erro na tentativa de mesclagem de objetos", ex);

                    if (tx != null) {
                        tx.rollback();
                    }
                }
            }
            else {
                result.include("msg", new ErrorMessage("Os objeto de origem e destino são o mesmo objeto."));
            }
        }
        else {
            result.include("msg", new ErrorMessage("Dados incompletos para masclar objetos"));
        }

        result.forwardTo(this).conceitos(elicitacao);
    }

    /**
     * Excluir conceito
     *
     * @param elicitacao Elicitação
     * @param objeto Objeto 1
     */
    @Post("/elicitacoes/{elicitacao.code}/conceitos/remover")
    public void removerConceito(Elicitacao elicitacao, Objeto objeto) {
        if (objeto == null || objeto.getId() == null) {
            result.include("msg", new ErrorMessage("Dados incompletos para excluir o objeto"));
        }
        else {
            objeto = objetoService.buscarObjetoPorId(objeto.getId());

            Transaction tx = null;
            try {
                elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

                tx = objetoService.getDao().getSession().beginTransaction();

                for (Relacao relacao : objeto.getRelacoesOrigem()) {
                    relacao.setOrigem(null);
                    relacaoService.update(relacao);
                }
                objeto.getRelacoesOrigem().clear();

                for (Relacao relacao : objeto.getRelacoesDestino()) {
                    relacao.setDestino(null);
                    relacaoService.update(relacao);
                }
                objeto.getRelacoesDestino().clear();

                objetoService.excluirObjeto(objeto);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Conceito #%d removido", objeto.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na remoção do conceito."));
                logger.error("Erro na tentativa de remoção de conceito", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).conceitos(elicitacao);
    }

    /**
     * Alterar conceito
     *
     * @param elicitacao Elicitação
     * @param objeto Objeto 1
     */
    @Post("/elicitacoes/{elicitacao.code}/conceitos/alterar")
    public void alterarConceito(Elicitacao elicitacao, Objeto objeto) {
        Validator val = new ObjetoAtualizacaoValidator(objeto);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para alterar o objeto"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            String nome = objeto.getNome();

            objeto = objetoService.buscarObjetoPorId(objeto.getId());

            Transaction tx = null;
            try {
                tx = objetoService.getDao().getSession().beginTransaction();

                objeto.setNome(nome);

                objetoService.update(objeto);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Conceito #%d alteradao", objeto.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na alteração do conceito."));
                logger.error("Erro na tentativa de alteração do objeto", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).conceitos(elicitacao);
    }

    /**
     * Definir a posição do conceito em relação ao agente
     *
     * @param elicitacao Elicitação
     * @param objeto Objeto
     */
    @Post("/elicitacoes/{elicitacao.code}/conceitos/definirPosicaoAgente")
    public void definirPosicaoAgente(Elicitacao elicitacao, Objeto objeto) {
        Validator val = new ObjetoControlabilidadeAgenteValidator(objeto);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para alterar da posição do objeto em relação ao agente"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            Controlabilidade controlabilidade = objeto.getControlabilidadeAgente();

            objeto = objetoService.buscarObjetoPorId(objeto.getId());

            Transaction tx = null;
            try {
                tx = objetoService.getDao().getSession().beginTransaction();

                objeto.setControlabilidadeAgente(controlabilidade);

                objetoService.update(objeto);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Posição em relação ao agente definido para o objeto #%d", objeto.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na alteração da posição do conceito em relação ao agente."));
                logger.error("Erro na tentativa de alteração da posição do objeto em relação ao agente", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).conceitos(elicitacao);
    }

    /**
     * Definir a posição do conceito em relação ao domínio
     *
     * @param elicitacao Elicitação
     * @param objeto Objeto
     */
    @Post("/elicitacoes/{elicitacao.code}/conceitos/definirPosicaoDominio")
    public void definirPosicaoDominio(Elicitacao elicitacao, Objeto objeto) {
        Validator val = new ObjetoControlabilidadeDominioValidator(objeto);

        if (val.hasErrors()) {
            result.include("msg", new ErrorMessage("Dados incompletos para alterar da posição do objeto em relação ao domínio"));
        }
        else {
            elicitacao = elicitacaoService.buscarElicitacaoPorId(elicitacao.getId());

            Controlabilidade controlabilidade = objeto.getControlabilidadeDominio();

            objeto = objetoService.buscarObjetoPorId(objeto.getId());

            Transaction tx = null;
            try {
                tx = objetoService.getDao().getSession().beginTransaction();

                objeto.setControlabilidadeDominio(controlabilidade);

                objetoService.update(objeto);

                elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                elicitacaoService.update(elicitacao);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Posição em relação ao domínio definido para o objeto #%d", objeto.getId());
            }
            catch (Exception ex) {
                result.include("msg", new ErrorMessage("Houve um erro na alteração da posição do conceito em relação ao domínio."));
                logger.error("Erro na tentativa de alteração da posição do objeto em relação ao agente", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.forwardTo(this).conceitos(elicitacao);
    }
}
