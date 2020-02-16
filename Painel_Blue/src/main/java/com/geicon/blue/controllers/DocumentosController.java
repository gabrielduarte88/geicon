package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import static br.com.caelum.vraptor.view.Results.json;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.framework.messages.ErrorMessage;
import com.geicon.blue.framework.messages.ExceptionMessage;
import com.geicon.blue.framework.messages.Message;
import com.geicon.blue.framework.messages.SuccessMessage;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.services.api.AgenteService;
import com.geicon.blue.services.api.DocumentoService;
import com.geicon.blue.services.api.LogService;
import com.geicon.blue.services.api.ObjetoOcorrenciaService;
import com.geicon.blue.services.api.RelacaoService;
import com.geicon.blue.validation.DocumentoValidator;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.Transaction;

/**
 * Controller do gerenciador de documentos
 *
 * @author Gabriel Duarte
 */
@Controller
public class DocumentosController implements Serializable {
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
     * Serviços de agentes
     */
    @Inject
    private AgenteService agenteService;
    /**
     * Serviços de documentos
     */
    @Inject
    private DocumentoService documentoService;
    /**
     * Serviços de ocorrências de objetos
     */
    @Inject
    private ObjetoOcorrenciaService objetoOcorrenciaService;
    /**
     * Serviços de relações
     */
    @Inject
    private RelacaoService relacaoService;
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
     * @param agente Agente
     */
    @Get("/agentes/{agente.code}/documentos")
    public void main(Agente agente) {
        result.include("agente", agenteService.buscarAgentePorId(agente.getId()));
    }

    /**
     * Listar documentos
     *
     * @param agente Agente
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @throws java.text.ParseException
     */
    @Post("/agentes/{agente.code}/documentos")
    public void listar(Agente agente, String filterField, String filterValue, String filterType, String orderField, String order, Integer page) throws ParseException {
        Integer itensPorPagina = Integer.parseInt(environment.get("maxlistitems"));

        SearchResult<Documento> searchResult = documentoService.listarDocumentos(agente, filterField, filterValue, filterType, orderField, order, page, itensPorPagina);

        result.include("pagina", page);
        result.include("itensPorPagina", itensPorPagina);

        result.include("campoFiltro", filterField);
        result.include("valorFiltro", filterValue);

        result.include("campoOrdem", orderField);
        result.include("ordem", order);

        result.include("documentos", searchResult.getItems());
        result.include("total", searchResult.getTotal());
    }

    /**
     * Novo documento
     *
     * @param agente Agente
     */
    @Get
    @Path(value = "/agentes/{agente.code}/documentos/novo", priority = Path.HIGH)
    public void novo(Agente agente) {
        result.include("agente", agenteService.buscarAgentePorId(agente.getId()));
    }

    /**
     * Cadastrar documento
     *
     * @param agente Agente
     * @param documento Novo documento
     */
    @Post("/agentes/{agente.code}/documentos/novo")
    public void inserir(Agente agente, Documento documento) {
        Message msg;

        Validator val = new DocumentoValidator(documento);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                documento.setAgente(agente);

                tx = documentoService.getDao().getSession().beginTransaction();

                documentoService.inserirDocumento(documento);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Documento #%d inserido", documento.getId());

                msg = new SuccessMessage("Documento inserido com sucesso!", documento.getCode());
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a inserção não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de inserção de documento", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Visualizar documento
     *
     * @param agente Agente
     * @param documento Documento visualizado
     */
    @Get
    @Path(value = "/agentes/{agente.code}/documentos/{documento.code}", priority = Path.LOW)
    public void visualizar(Agente agente, Documento documento) {
        if (documento != null && documento.getId() != null) {
            documento = documentoService.buscarDocumentoPorId(documento.getId());

            if (documento != null) {
                result.include("documento", documento);

                if (objetoOcorrenciaService.contarOcorrenciasPorDocumento(documento) > 0 || relacaoService.contarRelacoesPorDocumento(documento) > 0) {
                    result.include("bloqueado", true);
                }
            }
            else {
                result.redirectTo(this).main(agente);
            }
        }
        else {
            result.redirectTo(this).main(agente);
        }
    }

    /**
     * Alterar documento
     *
     * @param agente Agente
     * @param documento Documento alterado
     */
    @Post("/agentes/{agente.code}/documentos/{documento.code}/alterar")
    public void alterar(Agente agente, Documento documento) {
        Message msg;

        Validator val = new DocumentoValidator(documento);

        Transaction tx = null;
        if (val.hasErrors()) {
            msg = new ErrorMessage("Alguns erros foram encontrados:", val.getErrors());
        }
        else {
            try {
                if (documento.getId() != null) {
                    Documento documentoAt = documentoService.buscarDocumentoPorId(documento.getId());

                    documentoAt.setNome(documento.getNome());
                    documentoAt.setTexto(documento.getTexto());

                    tx = documentoService.getDao().getSession().beginTransaction();

                    documentoService.update(documentoAt);

                    tx.commit();

                    logService.log((Instituicao) result.included().get("instituicao"), "Documento #%d alterado", documentoAt.getId());

                    msg = new SuccessMessage("Documento alterado com sucesso!");
                }
                else {
                    msg = new ErrorMessage("Os dados necessários não foram enviados");
                }
            }
            catch (Exception ex) {
                msg = new ExceptionMessage("Houve um erro e a alteração não pode ser realizada. Por favor, tente novamente em breve.");
                logger.error("Erro na tentativa de alteração de documento", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Remover documento
     *
     * @param agente Agente
     * @param documento Documento excluido
     */
    @Post("/agentes/{agente.code}/documentos/{documento.code}/remover")
    public void excluir(Agente agente, Documento documento) {
        Message msg;

        Transaction tx = null;

        try {
            if (documento.getId() != null) {
                Documento documentoAt = documentoService.buscarDocumentoPorId(documento.getId());

                tx = documentoService.getDao().getSession().beginTransaction();

                documentoService.excluirDocumento(documentoAt);

                tx.commit();

                logService.log((Instituicao) result.included().get("instituicao"), "Documento #%d removido", documentoAt.getId());

                msg = new SuccessMessage("Documento removido com sucesso!");
            }
            else {
                msg = new ErrorMessage("Os dados necessários não foram enviados");
            }
        }
        catch (Exception ex) {
            msg = new ExceptionMessage("Houve um erro e a remoção não pode ser realizada. Por favor, tente novamente em breve.");
            logger.error("Erro na tentativa de remoção de documento", ex);

            if (tx != null) {
                tx.rollback();
            }
        }

        result.use(json()).from(msg, "msg").include("extra").serialize();
    }

    /**
     * Leitura de documentos
     *
     * @param agente Agente
     */
    @Get("/agentes/{agente.code}/documentos/leitura-documento")
    public void leituraDocumento(Agente agente) {
        result.include("agente", agenteService.buscarAgentePorId(agente.getId()));
    }

    /**
     * Leitura de documentos
     *
     * @param agente Agente
     * @param arquivo Arquivo enviado
     */
    @Post("/agentes/{agente.code}/documentos/leitura-documento")
    public void leituraDocumentoExecutar(Agente agente, UploadedFile arquivo) {
        if (arquivo != null) {
            try {
                InputStream arquivoConteudo = arquivo.getFile();
                String arquivoNome = arquivo.getFileName();
                String ext = arquivoNome.substring(arquivoNome.lastIndexOf('.'), arquivoNome.length());

                Set<String> data = new LinkedHashSet<>(0);

                switch (ext) {
                    case ".txt": {
                        BufferedReader br = new BufferedReader(new InputStreamReader(arquivoConteudo));

                        String line;
                        while ((line = br.readLine()) != null) {
                            data.add(line);
                        }
                    }
                    break;
                    case ".pdf": {
                        PdfReader reader = new PdfReader(arquivoConteudo);

                        for (int i = 0; i < reader.getNumberOfPages(); i++) {
                            data.add(PdfTextExtractor.getTextFromPage(reader, i + 1));
                        }
                    }
                    break;
                    case ".docx": {
                        XWPFDocument doc = new XWPFDocument(arquivoConteudo);
                        XWPFWordExtractor ex = new XWPFWordExtractor(doc);

                        data.add(ex.getText());
                    }
                    break;
                    case ".doc": {
                        WordExtractor wordExtractor = new WordExtractor(arquivoConteudo);
                        data.addAll(Arrays.asList(wordExtractor.getParagraphText()));
                    }
                }

                result.include("data", new Gson().toJson(data));
            }
            catch (IOException ex) {
                logger.error("Erro na leitura de arquivo", ex);
            }
        }

        result.forwardTo(this).leituraDocumento(agente);
    }
}
