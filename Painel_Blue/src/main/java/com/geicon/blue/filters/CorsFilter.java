package com.geicon.blue.filters;

import br.com.caelum.vraptor.environment.Environment;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Filtro JLight
 *
 * @author Gabriel Duarte
 */
@WebFilter(filterName = "CorsFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST})
public class CorsFilter implements Filter {
    /**
     * Ambiente
     */
    @Inject
    private Environment environment;

    /**
     * Inicio do filtro
     *
     * @param filterConfig Configuração do filtro
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    /**
     * Execução do filtro
     *
     * @param req Request HTTP
     * @param resp Response HTTP
     * @param chain FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        ((HttpServletResponse) resp).setHeader("Access-Control-Allow-Origin", environment.get("accessControlAllowOrigin"));
        ((HttpServletResponse) resp).addHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) resp).addHeader("Access-Control-Expose-Headers", "Content-Type, Location");

        chain.doFilter(req, resp);
    }

    /**
     * Destroy
     */
    @Override
    public void destroy() {
        //
    }
}
