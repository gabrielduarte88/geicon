package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Responsabilidade
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
public class Responsabilidade extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Usuário
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
    /**
     * Pesquisa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesquisa", nullable = false)
    private Pesquisa pesquisa;
    /**
     * Ação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acao", nullable = false)
    private Acao acao;
    /**
     * Data de cadastro
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;
    /**
     * Data de exclusão
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "excluido")
    private Date excluido;

    /**
     * Construtor
     */
    public Responsabilidade() {
    }

    /**
     * Obtém o usuário
     *
     * @return Usuário
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Altera o usuário
     *
     * @param usuario Usuário
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtém a pesquisa
     *
     * @return Pesquisa
     */
    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    /**
     * Altera a pesquisa
     *
     * @param pesquisa Pesquisa
     */
    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    /**
     * Obtém a ação
     *
     * @return Ação
     */
    public Acao getAcao() {
        return acao;
    }

    /**
     * Altera a ação
     *
     * @param acao Ação
     */
    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    /**
     * Obtém a data de cadastro
     *
     * @return Data de cadastro
     */
    public Date getData() {
        return data;
    }

    /**
     * Altera a data de cadastro
     *
     * @param data Data de cadastro
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Obtém a data de exclusão
     *
     * @return Data de exclusão
     */
    public Date getExcluido() {
        return excluido;
    }

    /**
     * Altera a data de exclusão
     *
     * @param excluido Data de exclusão
     */
    public void setExcluido(Date excluido) {
        this.excluido = excluido;
    }

    @Override
    public String toString() {
        return new StringBuilder("Responsabilidade{").append("id=").append(id).append(", usuario=").append(usuario).append(", pesquisa=").append(pesquisa).append(", acao=").append(acao).append(", data=").append(data).append(", excluido=").append(excluido).append('}').toString();
    }

}
