package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Base de conhecimento
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "BaseConhecimento", catalog = "dbKMS")
public class BaseConhecimento extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Pesquisa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesquisa", nullable = false)
    private Pesquisa pesquisa;
    /**
     * Título
     */
    @Column(name = "titulo", nullable = false, length = 60)
    private String titulo;
    /**
     * Proposição inicial
     */
    @Column(name = "proposicaoInicial", nullable = false, columnDefinition = "TEXT")
    private String proposicaoInicial;
    /**
     * Descrição
     */
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
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
     * Lista de elicitações
     */
    @OneToMany(mappedBy = "base")
    private Set<Elicitacao> elicitacoes = new LinkedHashSet<>(0);
    /**
     * Lista de agentes
     */
    @OneToMany(mappedBy = "base")
    private Set<Agente> agentes = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public BaseConhecimento() {
    }

    /**
     * Obtém pesquisa
     *
     * @return Pesquisa
     */
    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    /**
     * Altera pesquisa
     *
     * @param pesquisa Pesquisa
     */
    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    /**
     * Obtém título
     *
     * @return Título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Altera título
     *
     * @param titulo Título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém proposição inicial
     *
     * @return Proposição inicial
     */
    public String getProposicaoInicial() {
        return proposicaoInicial;
    }

    /**
     * Altera proposição inicial
     *
     * @param proposicaoInicial Proposição inicial
     */
    public void setProposicaoInicial(String proposicaoInicial) {
        this.proposicaoInicial = proposicaoInicial;
    }

    /**
     * Obtém descrição
     *
     * @return Descrição
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Altera descrição
     *
     * @param descricao Descrição
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém data de cadastro
     *
     * @return Data de cadastro
     */
    public Date getData() {
        return data;
    }

    /**
     * Altera data de cadastro
     *
     * @param data Data de cadastro
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Obtém data de exclusão
     *
     * @return Data de exclusão
     */
    public Date getExcluido() {
        return excluido;
    }

    /**
     * Altera data de exclusão
     *
     * @param excluido Data de exclusão
     */
    public void setExcluido(Date excluido) {
        this.excluido = excluido;
    }

    /**
     * Obtém a lista de elicitações
     *
     * @return Lista de elicitações
     */
    public Set<Elicitacao> getElicitacoes() {
        return elicitacoes;
    }

    /**
     * Altera a lista de elicitações
     *
     * @param elicitacoes Lista de elicitações
     */
    public void setElicitacoes(Set<Elicitacao> elicitacoes) {
        this.elicitacoes = elicitacoes;
    }

    /**
     * Obtém a lista de agentes
     *
     * @return Lista de agentes
     */
    public Set<Agente> getAgentes() {
        return agentes;
    }

    /**
     * Altera a lista de agentes
     *
     * @param agentes Lista de agentes
     */
    public void setAgentes(Set<Agente> agentes) {
        this.agentes = agentes;
    }

    @Override
    public String toString() {
        return new StringBuilder("BaseConhecimento{").append("id=").append(id).append(", pesquisa=").append(pesquisa).append(", titulo=").append(titulo).append(", proposicaoInicial=").append(proposicaoInicial).append(", descricao=").append(descricao).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
