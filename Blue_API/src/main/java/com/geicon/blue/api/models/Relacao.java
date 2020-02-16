package com.geicon.blue.api.models;

import com.geicon.blue.api.models.enums.RelacaoPeso;
import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Relação
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Relacao", catalog = "dbKMS")
public class Relacao extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Elicitação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elicitacao", nullable = false)
    private Elicitacao elicitacao;
    /**
     * Documento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento")
    private Documento documento;
    /**
     * Origem
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem")
    private Objeto origem;
    /**
     * Destino
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino")
    private Objeto destino;
    /**
     * Nome
     */
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;
    /**
     * Peso
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "peso")
    private RelacaoPeso peso;
    /**
     * Posição inicial
     */
    @Column(name = "posicaoInicial")
    private Integer posicaoInicial;
    /**
     * Posição final
     */
    @Column(name = "posicaoFinal")
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
    public Relacao() {
    }

    /**
     * Obtém elicitação
     *
     * @return Elicitação
     */
    public Elicitacao getElicitacao() {
        return elicitacao;
    }

    /**
     * Altera elicitação
     *
     * @param elicitacao Elicitação
     */
    public void setElicitacao(Elicitacao elicitacao) {
        this.elicitacao = elicitacao;
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
     * Obtém origem
     *
     * @return Origem
     */
    public Objeto getOrigem() {
        return origem;
    }

    /**
     * Altera origem
     *
     * @param origem Origem
     */
    public void setOrigem(Objeto origem) {
        this.origem = origem;
    }

    /**
     * Obtém destino
     *
     * @return Destino
     */
    public Objeto getDestino() {
        return destino;
    }

    /**
     * Altera destino
     *
     * @param destino Destino
     */
    public void setDestino(Objeto destino) {
        this.destino = destino;
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
     * Obtém peso
     *
     * @return Peso
     */
    public RelacaoPeso getPeso() {
        return peso;
    }

    /**
     * Altera peso
     *
     * @param peso Peso
     */
    public void setPeso(RelacaoPeso peso) {
        this.peso = peso;
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
        return new StringBuilder("Relacao{").append("id=").append(id).append(", origem=").append(origem).append(", documento=").append(documento).append(", destino=").append(destino).append(", nome=").append(nome).append(", peso=").append(peso).append(", posicaoInicial=").append(posicaoInicial).append(", posicaoFinal=").append(posicaoFinal).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
