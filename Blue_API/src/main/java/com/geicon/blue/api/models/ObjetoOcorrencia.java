package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Ocorrência de objeto
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "ObjetoOcorrencia", catalog = "dbKMS")
public class ObjetoOcorrencia extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Objeto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objeto", nullable = false)
    private Objeto objeto;
    /**
     * Documento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento", nullable = false)
    private Documento documento;
    /**
     * Valor
     */
    @Column(name = "valor", length = 60)
    private String valor;
    /**
     * Posição inicial
     */
    @Column(name = "posicaoInicial", nullable = false)
    private Integer posicaoInicial;
    /**
     * Posição final
     */
    @Column(name = "posicaoFinal", nullable = false)
    private Integer posicaoFinal;
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
    public ObjetoOcorrencia() {
    }

    /**
     * Obtém objeto
     *
     * @return Objeto
     */
    public Objeto getObjeto() {
        return objeto;
    }

    /**
     * Altera objeto
     *
     * @param objeto Objeto
     */
    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    /**
     * Obtém documento
     *
     * @return Documento
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * Altera documento
     *
     * @param documento Documento
     */
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    /**
     * Obtém valor
     *
     * @return Valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * Altera valor
     *
     * @param valor Valor
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Obtém posição inicial
     *
     * @return Posição inicial
     */
    public Integer getPosicaoInicial() {
        return posicaoInicial;
    }

    /**
     * Altera posição inicial
     *
     * @param posicaoInicial Posição inicial
     */
    public void setPosicaoInicial(Integer posicaoInicial) {
        this.posicaoInicial = posicaoInicial;
    }

    /**
     * Obtém posição final
     *
     * @return Posição final
     */
    public Integer getPosicaoFinal() {
        return posicaoFinal;
    }

    /**
     * Altera posição final
     *
     * @param posicaoFinal Posição final
     */
    public void setPosicaoFinal(Integer posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
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

    @Override
    public String toString() {
        return new StringBuilder("ObjetoOcorrencia{").append("id=").append(id).append(", objeto=").append(objeto).append(", documento=").append(documento).append(", valor=").append(valor).append(", posicaoInicial=").append(posicaoInicial).append(", posicaoFinal=").append(posicaoFinal).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
