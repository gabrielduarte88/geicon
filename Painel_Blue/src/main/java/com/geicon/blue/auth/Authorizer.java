package com.geicon.blue.auth;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.controllers.DashboardController;
import com.geicon.blue.framework.persistence.models.Entity;
import com.geicon.blue.services.api.AgenteService;
import com.geicon.blue.services.api.BaseConhecimentoService;
import com.geicon.blue.services.api.DocumentoService;
import com.geicon.blue.services.api.ElicitacaoService;
import com.geicon.blue.services.api.ObjetoService;
import com.geicon.blue.services.api.PesquisaService;
import com.geicon.blue.services.api.RelacaoService;
import com.geicon.blue.services.api.UsuarioService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Autorizador
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class Authorizer {
    /**
     * HTTP Request
     */
    @Inject
    private MethodInfo methodInfo;
    /**
     * Resultado da requisição
     */
    @Inject
    private Result result;
    /**
     * Serviços de usuário
     */
    @Inject
    private UsuarioService usuarioService;
    /**
     * Serviços de pesquisa
     */
    @Inject
    private PesquisaService pesquisaService;
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
     * Serviços de documentos
     */
    @Inject
    private DocumentoService documentoService;
    /**
     * Serviços de elicitação
     */
    @Inject
    private ElicitacaoService elicitacaoService;
    /**
     * Serviços de relações
     */
    @Inject
    private RelacaoService relacaoService;
    /**
     * Serviços de objetos
     */
    @Inject
    private ObjetoService objetoService;

    /**
     * Construtor
     */
    public Authorizer() {
    }

    /**
     * Autorização para UsuarioController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forUsuariosController(SimpleInterceptorStack stack, ControllerMethod method) {
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");

        if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else {
            result.redirectTo(DashboardController.class).main();
        }
    }

    /**
     * Autorização para PesquisasController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forPesquisasController(SimpleInterceptorStack stack, ControllerMethod method) {
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");

        Instituicao instituicao = (Instituicao) result.included().get("instituicao");

        if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else if (pesquisaService.isResponsavelPesquisas(instituicao, usuarioAtual)) {
            String methodName = method.getMethod().getName();

            if (methodName.equals("main") || methodName.equals("listar")) {
                stack.next();
            }
            else if (methodName.equals("visualizar") || methodName.equals("alterar")
                    || methodName.equals("participantes") || methodName.equals("salvarParticipantes")) {
                Pesquisa pesquisa = pesquisaService.buscarPesquisaPorId(instituicao, ((Entity) methodInfo.getParametersValues()[0]).getId());

                if (pesquisa.getResponsavel().equals(usuarioAtual)) {
                    stack.next();
                }
                else {
                    result.redirectTo(DashboardController.class).main();
                }
            }
            else {
                result.redirectTo(DashboardController.class).main();
            }
        }
        else {
            result.redirectTo(DashboardController.class).main();
        }
    }

    /**
     * Autorização para BasesConhecimentoController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forBasesConhecimentoController(SimpleInterceptorStack stack, ControllerMethod method) {
        result.include("dependentePesquisa", true);

        Instituicao instituicao = (Instituicao) result.included().get("instituicao");
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (pesquisaAtual == null) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (!pesquisaAtual.getInstituicao().equals(instituicao)) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else {
            String methodName = method.getMethod().getName();

            Pesquisa pesquisaVerificada = null;

            if (methodName.equals("main") || methodName.equals("listar") || methodName.equals("novo") || methodName.equals("inserir")) {
                pesquisaVerificada = pesquisaAtual;
            }
            else if (methodName.equals("visualizar") || methodName.equals("alterar") || methodName.equals("excluir")) {
                BaseConhecimento base = baseConhecimentoService.buscarBasePorId(((Entity) methodInfo.getParametersValues()[0]).getId());

                pesquisaVerificada = base.getPesquisa();
            }

            if (pesquisaVerificada != null) {
                if (pesquisaService.possuiResponsabilidade(instituicao, pesquisaVerificada, usuarioAtual, "Adm. bases de conhecimento")) {
                    stack.next();
                }
                else {
                    result.redirectTo(DashboardController.class).main();
                }
            }
            else {
                result.redirectTo(DashboardController.class).main();
            }
        }
    }

    /**
     * Autorização para AgentesController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forAgentesController(SimpleInterceptorStack stack, ControllerMethod method) {
        result.include("dependentePesquisa", true);

        Instituicao instituicao = (Instituicao) result.included().get("instituicao");
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (pesquisaAtual == null) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (!pesquisaAtual.getInstituicao().equals(instituicao)) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else {
            String methodName = method.getMethod().getName();

            Pesquisa pesquisaVerificada = null;

            if (methodName.equals("main") || methodName.equals("listar") || methodName.equals("novo") || methodName.equals("inserir")) {
                pesquisaVerificada = pesquisaAtual;
            }
            else if (methodName.equals("visualizar") || methodName.equals("alterar") || methodName.equals("excluir")) {
                Agente agente = agenteService.buscarAgentePorId(((Entity) methodInfo.getParametersValues()[0]).getId());

                if (agente != null && agente.getBase() != null) {
                    pesquisaVerificada = agente.getBase().getPesquisa();
                }
            }

            if (pesquisaVerificada != null) {
                if (pesquisaService.possuiResponsabilidade(instituicao, pesquisaVerificada, usuarioAtual, "Adm. agentes")) {
                    stack.next();
                }
                else {
                    result.redirectTo(DashboardController.class).main();
                }
            }
            else {
                result.redirectTo(DashboardController.class).main();
            }
        }
    }

    /**
     * Autorização para DocumentoController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forDocumentoController(SimpleInterceptorStack stack, ControllerMethod method) {
        result.include("dependentePesquisa", true);

        Instituicao instituicao = (Instituicao) result.included().get("instituicao");
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (pesquisaAtual == null) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (!pesquisaAtual.getInstituicao().equals(instituicao)) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else {
            String methodName = method.getMethod().getName();

            Pesquisa pesquisaVerificada = null;

            if (methodName.equals("main") || methodName.equals("listar") || methodName.equals("novo") || methodName.equals("inserir")
                    || methodName.equals("leituraDocumento") || methodName.equals("leituraDocumento")) {
                Agente agente = agenteService.buscarAgentePorId(((Entity) methodInfo.getParametersValues()[0]).getId());

                if (agente != null && agente.getBase() != null) {
                    pesquisaVerificada = agente.getBase().getPesquisa();
                }
            }
            else if (methodName.equals("visualizar") || methodName.equals("alterar") || methodName.equals("excluir")) {
                Documento documento = documentoService.buscarDocumentoPorId(((Entity) methodInfo.getParametersValues()[1]).getId());

                if (documento != null && documento.getAgente() != null && documento.getAgente().getBase() != null) {
                    pesquisaVerificada = documento.getAgente().getBase().getPesquisa();
                }
            }

            if (pesquisaVerificada != null) {
                if (pesquisaService.possuiResponsabilidade(instituicao, pesquisaVerificada, usuarioAtual, "Adm. documentos")) {
                    stack.next();
                }
                else {
                    result.redirectTo(DashboardController.class).main();
                }
            }
            else {
                result.redirectTo(DashboardController.class).main();
            }
        }
    }

    /**
     * Autorização para ElicitacoesController
     *
     * @param stack Stack
     * @param method Método do controller
     */
    public void forElicitacoesController(SimpleInterceptorStack stack, ControllerMethod method) {
        result.include("dependentePesquisa", true);

        Instituicao instituicao = (Instituicao) result.included().get("instituicao");
        Usuario usuarioAtual = (Usuario) result.included().get("usuarioAtual");
        Pesquisa pesquisaAtual = (Pesquisa) result.included().get("pesquisaAtual");

        if (pesquisaAtual == null) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (!pesquisaAtual.getInstituicao().equals(instituicao)) {
            result.redirectTo(DashboardController.class).main();
        }
        else if (usuarioService.isAdministrador(usuarioAtual)) {
            stack.next();
        }
        else {
            String methodName = method.getMethod().getName();

            Pesquisa pesquisaVerificada = null;

            if (methodName.equals("main") || methodName.equals("listar") || methodName.equals("novo") || methodName.equals("inserir")) {
                pesquisaVerificada = pesquisaAtual;
            }
            else if (methodName.equals("visualizar") || methodName.equals("excluir")
                    || methodName.equals("elicitar") || methodName.equals("proposicoes") || methodName.equals("adicionarProposicao")
                    || methodName.equals("conceitos") || methodName.equals("grafo") || methodName.equals("dados")) {
                Elicitacao elicitacao = elicitacaoService.buscarElicitacaoPorId(((Entity) methodInfo.getParametersValues()[0]).getId());

                if (elicitacao != null && elicitacao.getBase() != null) {
                    pesquisaVerificada = elicitacao.getBase().getPesquisa();
                }
            }
            else if (methodName.equals("alterarProposicao") || methodName.equals("definirPesoProposicao") || methodName.equals("removerProposicao")
                    || methodName.equals("adicionarConceito")) {
                Relacao relacao = relacaoService.buscarRelacaoPorId(((Entity) methodInfo.getParametersValues()[1]).getId());

                if (relacao != null && relacao.getElicitacao() != null && relacao.getElicitacao().getBase() != null && relacao.getElicitacao().getBase().getPesquisa() != null) {
                    pesquisaVerificada = relacao.getElicitacao().getBase().getPesquisa();
                }
            }
            else if (methodName.equals("mesclarConceito") || methodName.equals("removerConceito") || methodName.equals("alterarConceito")
                    || methodName.equals("definirPosicaoAgente") || methodName.equals("definirPosicaoDominio")) {
                Objeto objeto = objetoService.buscarObjetoPorId(((Entity) methodInfo.getParametersValues()[1]).getId());

                if (objeto != null && objeto.getElicitacao() != null && objeto.getElicitacao().getBase() != null && objeto.getElicitacao().getBase().getPesquisa() != null) {
                    pesquisaVerificada = objeto.getElicitacao().getBase().getPesquisa();
                }
            }

            if (pesquisaVerificada != null) {
                if (pesquisaService.possuiResponsabilidade(instituicao, pesquisaVerificada, usuarioAtual, "Elicitação")) {
                    stack.next();
                }
                else {
                    result.redirectTo(DashboardController.class).main();
                }
            }
            else {
                result.redirectTo(DashboardController.class).main();
            }
        }
    }
}
