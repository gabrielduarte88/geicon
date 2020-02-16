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
 * Documento
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Documento", catalog = "dbKMS")
public class Documento extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Agente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente", nullable = false)
    private Agente agente;
    /**
     * Nome
     */
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;
    /**
     * Texto
     */
    @Column(name = "texto", columnDefinition = "LONGTEXT")
    private String texto;
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
     * Lista de ocorrências de objetos
     */
    @OneToMany(mappedBy = "documento")
    private Set<ObjetoOcorrencia> ocorrenciasObjeto = new LinkedHashSet<>(0);
    /**
     * Lista de relações
     */
    @OneToMany(mappedBy = "documento")
    private Set<Relacao> relacoes = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Documento() {
    }

    /**
     * Obtém agente
     *
     * @return Agente
     */
    public Agente getAgente() {
        return agente;
    }

    /**
     * Altera agente
     *
     * @param agente Agente
     */
    public void setAgente(Agente agente) {
        this.agente = agente;
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
     * Obtém texto
     *
     * @return Texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Altera texto
     *
     * @param texto Texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
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
     * Obtém a lista de ocorrências de objetos
     *
     * @return Lista de ocorrências de objetos
     */
    public Set<ObjetoOcorrencia> getOcorrenciasObjeto() {
        return ocorrenciasObjeto;
    }

    /**
     * Altera a lista de ocorrências de objetos
     *
     * @param ocorrenciasObjeto Lista de ocorrências de objetos
     */
    public void setOcorrenciasObjeto(Set<ObjetoOcorrencia> ocorrenciasObjeto) {
        this.ocorrenciasObjeto = ocorrenciasObjeto;
    }

    /**
     * Obtém a lista de relações
     *
     * @return Lista de relações
     */
    public Set<Relacao> getRelacoes() {
        return relacoes;
    }

    /**
     * Altera a lista de relações
     *
     * @param relacoes Lista de relações
     */
    public void setRelacoes(Set<Relacao> relacoes) {
        this.relacoes = relacoes;
    }

    @Override
    public String toString() {
        return new StringBuilder("Documento{").append("id=").append(id).append(", agente=").append(agente).append(", nome=").append(nome).append(", texto=").append(texto).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
