package com.geicon.blue.interceptors;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import com.geicon.blue.annotations.Login;
import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.auth.Authorizer;
import com.geicon.blue.auth.UserSession;
import com.geicon.blue.controllers.DashboardController;
import com.geicon.blue.controllers.IndexController;
import com.geicon.blue.services.api.InstituicaoService;
import com.geicon.blue.services.api.PesquisaService;
import com.geicon.blue.services.api.UsuarioService;
import java.util.Arrays;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Interceptador principal
 *
 * @author Gabriel Duarte
 */
@Intercepts
@RequestScoped
public class AuthInterceptor {
    /**
     * Ambiente
     */
    @Inject
    private Environment environment;
    /**
     * Sessão do usuário
     */
    @Inject
    private UserSession userSession;
    /**
     * Autorizador
     */
    @Inject
    private Authorizer authorizer;
    /**
     * HTTP Request
     */
    @Inject
    private HttpServletRequest request;
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
     * Serviços de instituição
     */
    @Inject
    private InstituicaoService instituicaoService;
    /**
     * Serviços de pesquisa
     */
    @Inject
    private PesquisaService pesquisaService;

    /**
     * Interceptação
     *
     * @param stack Stack
     * @param method Método interceptado
     */
    @AroundCall
    public void intercept(SimpleInterceptorStack stack, ControllerMethod method) {
        //Nome do controller
        String controllerName = method.getController().getType().getSimpleName();

        String dominio = request.getServerName().split("\\.")[0];

        if (dominio != null && Arrays.asList(environment.get("homeSubdomains").split(",")).contains(dominio)) {
            if (controllerName.equals("IndexController") && (method.getMethod().getName().equals("index") || method.getMethod().getName().equals("interesse"))) {
                stack.next();
            }
            else {
                result.forwardTo(IndexController.class).index();
            }
        }
        else {
            Instituicao instituicao = instituicaoService.buscarInstituicaoPorDominio(dominio);

            if (instituicao == null) {
                result.redirectTo(environment.get("contextPath"));
            }
            else {
                result.include("instituicao", instituicao);

                Integer usuarioAtualId = userSession.getUsuario();

                //Autenticação
                if (method.getClass().isAnnotationPresent(Login.class) || method.containsAnnotation(Login.class)) {
                    if (usuarioAtualId != null) {
                        result.redirectTo(DashboardController.class).main();
                    }
                    else {
                        stack.next();
                    }
                }
                else if (usuarioAtualId == null) {
                    result.redirectTo(IndexController.class).main();
                }
                else {
                    Usuario usuarioAtual = usuarioService.buscarUsuarioPorId(instituicao, usuarioAtualId);

                    if (usuarioAtual == null) {
                        userSession.setUsuario(null);
                        result.redirectTo(IndexController.class).main();
                    }
                    else if (usuarioAtual != null && !usuarioAtual.getInstituicao().equals(instituicao)) {
                        userSession.setUsuario(null);
                        result.redirectTo(IndexController.class).main();
                    }
                    else {
                        result.include("usuarioAtual", usuarioAtual);

                        result.include("usuarioService", usuarioService);
                        result.include("pesquisaService", pesquisaService);

                        Set<Pesquisa> usuarioPesquisas = pesquisaService.listarPesquisasPorUsuario(instituicao, usuarioAtual);

                        usuarioPesquisas.stream().forEach((pesquisa) -> {
                            pesquisaService.getDao().getSession().evict(pesquisa);
                        });

                        result.include("usuarioPesquisas", usuarioPesquisas);

                        Integer pesquisaAtualId = userSession.getPesquisa();

                        if (pesquisaAtualId != null) {
                            Pesquisa pesquisaAtual = pesquisaService.buscarPesquisaPorId(instituicao, pesquisaAtualId);

                            if (pesquisaService.isParticipante(instituicao, pesquisaAtual, usuarioAtual)) {
                                result.include("pesquisaAtual", pesquisaAtual);
                            }
                            else {
                                userSession.setPesquisa(null);
                            }
                        }

                        //Autorização por classe/método
                        switch (controllerName) {
                            //Gerenciamento de usuários
                            case "UsuariosController": {
                                authorizer.forUsuariosController(stack, method);
                                break;
                            }
                            //Gerenciamento de pesquisas
                            case "PesquisasController": {
                                authorizer.forPesquisasController(stack, method);
                                break;
                            }
                            //Gerenciamento de bases de conhecimento
                            case "BasesConhecimentoController": {
                                authorizer.forBasesConhecimentoController(stack, method);
                                break;
                            }
                            //Gerenciamento de agentes
                            case "AgentesController": {
                                authorizer.forAgentesController(stack, method);
                                break;
                            }
                            //Gerenciamento de elicitações
                            case "ElicitacoesController": {
                                authorizer.forElicitacoesController(stack, method);
                                break;
                            }
                            //Gerenciamento de documentos
                            case "DocumentoController": {
                                authorizer.forDocumentoController(stack, method);
                                break;
                            }
                            default: {
                                stack.next();
                            }
                        }
                    }
                }
            }
        }
    }
}
