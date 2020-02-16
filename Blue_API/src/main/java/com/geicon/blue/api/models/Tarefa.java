package com.geicon.blue.api.models;

import com.geicon.blue.api.models.enums.TarefaStatus;
import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tarefa
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
public class Tarefa extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Método
     */
    @Column(name = "metodo", nullable = false, length = 60)
    private String metodo;
    /**
     * Parâmetros
     */
    @Column(name = "parametros", length = 100)
    private String parametros;
    /**
     * Intervalo
     */
    @Column(name = "intervalo")
    private Integer intervalo;
    /**
     * Data de cadastro
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;
    /**
     * Data de início
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inicio")
    private Date inicio;
    /**
     * Data de término
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "termino")
    private Date termino;
    /**
     * Status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 60)
    private TarefaStatus status;
    /**
     * Mensagem
     */
    @Column(name = "mensagem", columnDefinition = "TEXT")
    private String mensagem;

    /**
     * Construtor
     */
    public Tarefa() {
    }

    /**
     * Obtém o método
     *
     * @return Método
     */
    public String getMetodo() {
        return metodo;
    }

    /**
     * Altera o método
     *
     * @param metodo Método
     */
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    /**
     * Obtém os parâmetros
     *
     * @return Parâmetros
     */
    public String getParametros() {
        return parametros;
    }

    /**
     * Altera os parâmetros
     *
     * @param parametros Parâmetros
     */
    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    /**
     * Obtém o intervalo
     *
     * @return Intervalo
     */
    public Integer getIntervalo() {
        return intervalo;
    }

    /**
     * Altera o intervalo
     *
     * @param intervalo Intervalo
     */
    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
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
     * Obtém a data de início
     *
     * @return Data de início
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * Altera a data de início
     *
     * @param inicio Data de início
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * Obtém a data de término
     *
     * @return Data de término
     */
    public Date getTermino() {
        return termino;
    }

    /**
     * Altera a data de término
     *
     * @param termino Data de término
     */
    public void setTermino(Date termino) {
        this.termino = termino;
    }

    /**
     * Obtém o status
     *
     * @return Status
     */
    public TarefaStatus getStatus() {
        return status;
    }

    /**
     * Altera o status
     *
     * @param status Status
     */
    public void setStatus(TarefaStatus status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return new StringBuilder("Tarefa{").append("id=").append(id).append(", metodo=").append(metodo).append(", parametros=").append(parametros).append(", intervalo=").append(intervalo).append(", data=").append(data).append(", inicio=").append(inicio).append(", termino=").append(termino).append(", status=").append(status).append(", mensagem=").append(mensagem).append('}').toString();
    }
}
