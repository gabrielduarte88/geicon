package com.geicon.blue.auth;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 * Sessão do usuário
 *
 * @author Gabriel
 */
@SessionScoped
public class UserSession implements Serializable {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * Usuário
     */
    private Integer usuario;
    /**
     * Pesquisa
     */
    private Integer pesquisa;

    /**
     * Obtém o usuário
     *
     * @return Usuário
     */
    public Integer getUsuario() {
        return usuario;
    }

    /**
     * Altera o usuário
     *
     * @param usuario Usuário
     */
    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtém a pesquisa
     *
     * @return Pesquisa
     */
    public Integer getPesquisa() {
        return pesquisa;
    }

    /**
     * Altera a pesquisa
     *
     * @param pesquisa Pesquisa
     */
    public void setPesquisa(Integer pesquisa) {
        this.pesquisa = pesquisa;
    }
}
