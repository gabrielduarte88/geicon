package com.geicon.blue.interceptors;

import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import com.geicon.blue.api.models.Instituicao;
import java.io.UnsupportedEncodingException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptador para complementos
 *
 * @author Gabriel Duarte
 */
@Intercepts(after = AuthInterceptor.class)
@RequestScoped
public class ComplInterceptor {
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
     * HTTP Response
     */
    @Inject
    private HttpServletResponse response;
    /**
     * Result
     */
    @Inject
    private Result result;

    /**
     * Interceptação
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @BeforeCall
    public void intercept() throws UnsupportedEncodingException {
        //Context Path
        Instituicao instituicao = (Instituicao) result.included().get("instituicao");
        if (instituicao == null) {
            request.setAttribute("contextPath", environment.get("contextPath"));
        }
        else {
            result.include("contextPath", environment.get("domainContextPath").replace("###DOMAIN###", instituicao.getDominio()));
        }

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }
}
