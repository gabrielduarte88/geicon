package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Configuração
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
public class Configuracao extends GenericEntity {
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
     * Notificar alteração em pesquisas
     */
    @Column(name = "notificarAlteracaoPesquisa")
    private Boolean notificarAlteracaoPesquisa;
    /**
     * Notificar alteração em bases de conhecimetno
     */
    @Column(name = "notificarAlteracaoBase")
    private Boolean notificarAlteracaoBase;
    /**
     * Notificar alteração em agentes
     */
    @Column(name = "notificarAlteracaoAgente")
    private Boolean notificarAlteracaoAgente;
    /**
     * Notificar alteração em documentos
     */
    @Column(name = "notificarAlteracaoDocumento")
    private Boolean notificarAlteracaoDocumento;
    /**
     * Notificar alteração em elicitação
     */
    @Column(name = "notificarAlteracaoElicitacao")
    private Boolean notificarAlteracaoElicitacao;

    /**
     * Construtor
     */
    public Configuracao() {
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
     * Obtém indicador de notificação de alteração em pesquisas
     *
     * @return Indicador de notificação de alteração em pesquisas
     */
    public Boolean getNotificarAlteracaoPesquisa() {
        return notificarAlteracaoPesquisa;
    }

    /**
     * Altera o indicador de notificação de alteração em pesquisas
     *
     * @param notificarAlteracaoPesquisa Indicador de notificação de alteração
     * em pesquisas
     */
    public void setNotificarAlteracaoPesquisa(Boolean notificarAlteracaoPesquisa) {
        this.notificarAlteracaoPesquisa = notificarAlteracaoPesquisa;
    }

    /**
     * Obtém indicador de notificação de alteração em bases de conhecimento
     *
     * @return Indicador de notificação de alteração em bases de conhecimento
     */
    public Boolean getNotificarAlteracaoBase() {
        return notificarAlteracaoBase;
    }

    /**
     * Altera o indicador de notificação de alteração em bases de conhecimento
     *
     * @param notificarAlteracaoBase Indicador de notificação de alteração em
     * bases de conhecimento
     */
    public void setNotificarAlteracaoBase(Boolean notificarAlteracaoBase) {
        this.notificarAlteracaoBase = notificarAlteracaoBase;
    }

    /**
     * Obtém indicador de notificação de alteração em agentes
     *
     * @return Indicador de notificação de alteração em agentes
     */
    public Boolean getNotificarAlteracaoAgente() {
        return notificarAlteracaoAgente;
    }

    /**
     * Altera o indicador de notificação de alteração em agentes
     *
     * @param notificarAlteracaoAgente Indicador de notificação de alteração em
     * agentes
     */
    public void setNotificarAlteracaoAgente(Boolean notificarAlteracaoAgente) {
        this.notificarAlteracaoAgente = notificarAlteracaoAgente;
    }

    /**
     * Obtém indicador de notificação de alteração em documentos
     *
     * @return Indicador de notificação de alteração em documentos
     */
    public Boolean getNotificarAlteracaoDocumento() {
        return notificarAlteracaoDocumento;
    }

    /**
     * Altera o indicador de notificação de alteração em documentos
     *
     * @param notificarAlteracaoDocumento Indicador de notificação de alteração
     * em documentos
     */
    public void setNotificarAlteracaoDocumento(Boolean notificarAlteracaoDocumento) {
        this.notificarAlteracaoDocumento = notificarAlteracaoDocumento;
    }

    /**
     * Obtém indicador de notificação de alteração em elicitações
     *
     * @return Indicador de notificação de alteração em elicitações
     */
    public Boolean getNotificarAlteracaoElicitacao() {
        return notificarAlteracaoElicitacao;
    }

    /**
     * Altera o indicador de notificação de alteração em elicitações
     *
     * @param notificarAlteracaoElicitacao Indicador de notificação de alteração
     * em elicitações
     */
    public void setNotificarAlteracaoElicitacao(Boolean notificarAlteracaoElicitacao) {
        this.notificarAlteracaoElicitacao = notificarAlteracaoElicitacao;
    }

    @Override
    public String toString() {
        return new StringBuilder("Configuracao{").append("id=").append(id).append(", usuario=").append(usuario).append(", notificarAlteracaoPesquisa=").append(notificarAlteracaoPesquisa).append(", notificarAlteracaoBase=").append(notificarAlteracaoBase).append(", notificarAlteracaoAgente=").append(notificarAlteracaoAgente).append(", notificarAlteracaoDocumento=").append(notificarAlteracaoDocumento).append(", notificarAlteracaoElicitacao=").append(notificarAlteracaoElicitacao).append('}').toString();
    }

}
