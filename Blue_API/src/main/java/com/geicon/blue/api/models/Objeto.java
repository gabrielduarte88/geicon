package com.geicon.blue.api.models;

import com.geicon.blue.api.models.enums.Controlabilidade;
import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Objeto
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Objeto", catalog = "dbKMS")
public class Objeto extends GenericEntity {
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
     * Nome
     */
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;
    /**
     * Controlabilidade do agente
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "controlabilidadeAgente")
    private Controlabilidade controlabilidadeAgente;
    /**
     * Controlabilidade para o domínio
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "controlabilidadeDominio")
    private Controlabilidade controlabilidadeDominio;
    /**
     * Posição
     */
    @Column(name = "posicao")
    private Integer posicao;
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
    @OneToMany(mappedBy = "objeto")
    private Set<ObjetoOcorrencia> ocorrenciasObjeto = new LinkedHashSet<>(0);
    /**
     * Lista de relações de origem
     */
    @OneToMany(mappedBy = "origem")
    private Set<Relacao> relacoesOrigem = new LinkedHashSet<>(0);
    /**
     * Lista de relações de destino
     */
    @OneToMany(mappedBy = "destino")
    private Set<Relacao> relacoesDestino = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Objeto() {
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
     * Obtém controlabilidade do agente
     *
     * @return Controlabilidade do agente
     */
    public Controlabilidade getControlabilidadeAgente() {
        return controlabilidadeAgente;
    }

    /**
     * Altera controlabilidade do agente
     *
     * @param controlabilidadeAgente Controlabilidade do agente
     */
    public void setControlabilidadeAgente(Controlabilidade controlabilidadeAgente) {
        this.controlabilidadeAgente = controlabilidadeAgente;
    }

    /**
     * Obtém controlabilidade para o domínio
     *
     * @return Controlabilidade para o domínio
     */
    public Controlabilidade getControlabilidadeDominio() {
        return controlabilidadeDominio;
    }

    /**
     * Altera controlabilidade para o domínio
     *
     * @param controlabilidadeDominio Controlabilidade para o domínio
     */
    public void setControlabilidadeDominio(Controlabilidade controlabilidadeDominio) {
        this.controlabilidadeDominio = controlabilidadeDominio;
    }

    /**
     * Obtém posição
     *
     * @return Posição
     */
    public Integer getPosicao() {
        return posicao;
    }

    /**
     * Altera posição
     *
     * @param posicao Posição
     */
    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
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
     * Obtém a lista de relações de origem
     *
     * @return Lista de relações de origem
     */
    public Set<Relacao> getRelacoesOrigem() {
        return relacoesOrigem;
    }

    /**
     * Altera a lista de relações de origem
     *
     * @param relacoesOrigem Lista de relações de origem
     */
    public void setRelacoesOrigem(Set<Relacao> relacoesOrigem) {
        this.relacoesOrigem = relacoesOrigem;
    }

    /**
     * Obtém a lista de relações de destino
     *
     * @return Lista de relações de destino
     */
    public Set<Relacao> getRelacoesDestino() {
        return relacoesDestino;
    }

    /**
     * Altera a lista de relações de destino
     *
     * @param relacoesDestino Lista de relações de destino
     */
    public void setRelacoesDestino(Set<Relacao> relacoesDestino) {
        this.relacoesDestino = relacoesDestino;
    }

    @Override
    public String toString() {
        return new StringBuilder("Objeto{").append("id=").append(id).append(", elicitacao=").append(elicitacao).append(", nome=").append(nome).append(", controlabilidadeAgente=").append(controlabilidadeAgente).append(", controlabilidadeDominio=").append(controlabilidadeDominio).append(", posicao=").append(posicao).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
