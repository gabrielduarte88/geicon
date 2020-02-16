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
 * Notificação
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
public class Notificacao extends GenericEntity {
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
     * Categoria
     */
    @Column(name = "categoria", nullable = false, length = 60)
    private String categoria;
    /**
     * Título
     */
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    /**
     * Mensagem
     */
    @Column(name = "mensagem", columnDefinition = "TEXT")
    private String mensagem;
    /**
     * Data de cadastro
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    /**
     * Construtor
     */
    public Notificacao() {
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
     * Obtém a categoria
     *
     * @return Categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Altera a categoria
     *
     * @param categoria Categoria
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtém o título
     *
     * @return Título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Altera o título
     *
     * @param titulo Título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém a mensagem
     *
     * @return Mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Altera a mensagem
     *
     * @param mensagem Mensagem
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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

    @Override
    public String toString() {
        return new StringBuilder("Notificacao{").append("id=").append(id).append(", usuario=").append(usuario).append(", categoria=").append(categoria).append(", titulo=").append(titulo).append(", mensagem=").append(mensagem).append(", data=").append(data).append('}').toString();
    }

}
