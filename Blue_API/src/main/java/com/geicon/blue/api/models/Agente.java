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
 * Agente
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Agente", catalog = "dbKMS")
public class Agente extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Base de conhecimento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base", nullable = false)
    private BaseConhecimento base;
    /**
     * Nome
     */
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;
    /**
     * Descrição
     */
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
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
     * Lista de elicitacoes
     */
    @OneToMany(mappedBy = "agente")
    private Set<Elicitacao> elicitacoes = new LinkedHashSet<>(0);
    /**
     * Lista de documentos
     */
    @OneToMany(mappedBy = "agente")
    private Set<Documento> documentos = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Agente() {
    }

    /**
     * Obtém base de conhecimento
     *
     * @return Base de conhecimento
     */
    public BaseConhecimento getBase() {
        return base;
    }

    /**
     * Altera base de conhecimento
     *
     * @param base Base de conhecimento
     */
    public void setBase(BaseConhecimento base) {
        this.base = base;
    }

    /**
     * Obtém nome
     *
     * @return Nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera nome
     *
     * @param nome Nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * Obtém a lista de documentos
     *
     * @return Lista de documentos
     */
    public Set<Documento> getDocumentos() {
        return documentos;
    }

    /**
     * Altera a lista de documentos
     *
     * @param documentos Lista de documentos
     */
    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    @Override
    public String toString() {
        return new StringBuilder("Agente{").append("id=").append(id).append(", base=").append(base).append(", nome=").append(nome).append(", descricao=").append(descricao).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
