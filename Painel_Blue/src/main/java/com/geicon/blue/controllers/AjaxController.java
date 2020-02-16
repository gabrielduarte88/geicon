package com.geicon.blue.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.api.models.enums.Controlabilidade;
import com.geicon.blue.api.models.enums.RelacaoPeso;
import com.geicon.blue.services.api.AgenteService;
import com.geicon.blue.services.api.BaseConhecimentoService;
import com.geicon.blue.services.api.ElicitacaoService;
import com.geicon.blue.services.api.ObjetoService;
import com.geicon.blue.services.api.RelacaoService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Controller das atividades por AJAX
 *
 * @author Gabriel Duarte
 */
@Controller
public class AjaxController implements Serializable {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;
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
     * Serviços de elicitação
     */
    @Inject
    private ElicitacaoService elicitacaoService;
    /**
     * Serviços de objetos
     */
    @Inject
    private ObjetoService objetoService;
    /**
     * Serviços de relações
     */
    @Inject
    private RelacaoService relacaoService;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Agentes da base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento visualizada
     */
    @Get("/bases-conhecimento/{baseConhecimento.code}/agentes")
    public void agentes(BaseConhecimento baseConhecimento) {
        baseConhecimento = baseConhecimentoService.buscarBasePorId(baseConhecimento.getId());

        if (baseConhecimento != null) {
            result.include("agentes", agenteService.listarAgentesPorBaseDeConhecimento(baseConhecimento));
        }
    }

    /**
     * Gerar casos de teste
     */
    @Get("/gerar-teste/{baseConhecimentoId}/{agenteId}")
    public void gerarCasosTeste(Integer baseConhecimentoId, Integer agenteId) {
        BaseConhecimento baseConhecimento = baseConhecimentoService.buscarBasePorId(baseConhecimentoId);
        Agente agente = agenteService.buscarAgentePorId(agenteId);

        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");

        if (baseConhecimento != null && agente != null) {
            Transaction tx = null;

            try {
                tx = elicitacaoService.getDao().getSession().beginTransaction();

                for (int i = 1; i <= 20; i++) {
                    Elicitacao elicitacao = new Elicitacao();
                    elicitacao.setBase(baseConhecimento);
                    elicitacao.setResponsavel(usuarioAtual);
                    elicitacao.setAgente(agente);
                    elicitacao.setData(new Date());

                    elicitacaoService.insert(elicitacao);

                    List<Objeto> objetos = new ArrayList<>();
                    for (int j = 0; j < i * 5; j++) {
                        Objeto objeto = new Objeto();
                        objeto.setElicitacao(elicitacao);
                        objeto.setNome("Conceito " + j);
                        objeto.setControlabilidadeAgente(Controlabilidade.values()[new Random().nextInt(Controlabilidade.values().length)]);
                        objeto.setControlabilidadeDominio(Controlabilidade.values()[new Random().nextInt(Controlabilidade.values().length)]);
                        objeto.setData(new Date());

                        objetoService.insert(objeto);

                        objetos.add(objeto);
                    }

                    int r = 0;
                    for (Objeto obj : objetos) {
                        Objeto obj2 = buscarAleatorio(obj, objetos);

                        Relacao relacao = new Relacao();
                        relacao.setElicitacao(elicitacao);
                        relacao.setOrigem(obj);
                        relacao.setDestino(obj2);
                        relacao.setNome("Relacao " + (r++));
                        relacao.setPeso(RelacaoPeso.values()[new Random().nextInt(RelacaoPeso.values().length)]);
                        relacao.setData(new Date());

                        relacaoService.inserirRelacao(relacao);
                    }

                    elicitacao.setScore(elicitacaoService.calcularScore(elicitacao));
                    elicitacao.setScore2(elicitacaoService.calcularScore2(elicitacao));

                    elicitacaoService.update(elicitacao);
                }
            }
            catch (Exception ex) {
                logger.error("Erro na tentativa de criação da base de testes", ex);

                if (tx != null) {
                    tx.rollback();
                }
            }

            tx.commit();
        }
    }

    private Objeto buscarAleatorio(Objeto obj, List<Objeto> objetos) {
        Objeto obj2 = objetos.get(new Random().nextInt(objetos.size()));

        if (!obj.equals(obj2)) {
            return obj2;
        }

        return buscarAleatorio(obj, objetos);
    }
}
