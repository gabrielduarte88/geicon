package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Interesse
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Interesse", catalog = "dbKMS")
public class Interesse extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Nome
     */
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;
    /**
     * E-mail
     */
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    /**
     * Organização
     */
    @Column(name = "organizacao", nullable = false, length = 100)
    private String organizacao;

    /**
     * Data de cadastro
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data")
    private Date data;

    /**
     * Construtor
     */
    public Interesse() {
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
     * Obtém o e-mail
     *
     * @return E-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Altera o e-mail
     *
     * @param email E-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a organização
     *
     * @return Organização
     */
    public String getOrganizacao() {
        return organizacao;
    }

    /**
     * Altera a organização
     *
     * @param organizacao Organização
     */
    public void setOrganizacao(String organizacao) {
        this.organizacao = organizacao;
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
        return new StringBuilder("Interesse{").append("id=").append(id).append("nome=").append(nome).append(", email=").append(email).append(", organizacao=").append(organizacao).append(", data=").append(data).append('}').toString();
    }

}
