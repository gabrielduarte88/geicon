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
 * Elicitação
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Elicitacao", catalog = "dbKMS")
public class Elicitacao extends GenericEntity {
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
     * Responsável
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel", nullable = false)
    private Usuario responsavel;
    /**
     * Agente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente", nullable = false)
    private Agente agente;
    /**
     * Score
     */
    @Column(name = "score")
    private Float score;
    /**
     * Score 2
     */
    @Column(name = "score2")
    private Float score2;
    /**
     * Última alteração
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ultimaAlteracao")
    private Date ultimaAlteracao;
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
     * Lista de relações
     */
    @OneToMany(mappedBy = "elicitacao")
    private Set<Relacao> relacoes = new LinkedHashSet<>(0);
    /**
     * Lista de objetos
     */
    @OneToMany(mappedBy = "elicitacao")
    private Set<Objeto> objetos = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Elicitacao() {
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
     * Obtém responsável
     *
     * @return Responsável
     */
    public Usuario getResponsavel() {
        return responsavel;
    }

    /**
     * Altera responsável
     *
     * @param responsavel Responsável
     */
    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
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
     * Obtém score
     *
     * @return Score
     */
    public Float getScore() {
        return score;
    }

    /**
     * Altera score
     *
     * @param score Score
     */
    public void setScore(Float score) {
        this.score = score;
    }

    /**
     * Obtém score 2
     *
     * @return Score 2
     */
    public Float getScore2() {
        return score2;
    }

    /**
     * Altera score 2
     *
     * @param score2 Score 2
     */
    public void setScore2(Float score2) {
        this.score2 = score2;
    }

    /**
     * Obtém a data da última alteração
     *
     * @return Data da última alteração
     */
    public Date getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    /**
     * Altera a data da última alteração
     *
     * @param ultimaAlteracao Data da última alteração
     */
    public void setUltimaAlteracao(Date ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
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
     * Obtém a lista de objetos
     *
     * @return Lista de objetos
     */
    public Set<Objeto> getObjetos() {
        return objetos;
    }

    /**
     * Altera a lista de objetos
     *
     * @param objetos Lista de objetos
     */
    public void setObjetos(Set<Objeto> objetos) {
        this.objetos = objetos;
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
        return new StringBuilder("Elicitacao{").append("id=").append(id).append(", base=").append(base).append(", responsavel=").append(responsavel).append(", agente=").append(agente).append(", score=").append(score).append(", score2=").append(score2).append(", ultimaAlteracao=").append(ultimaAlteracao).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
