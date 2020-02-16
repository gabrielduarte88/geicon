package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Ação
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Acao", catalog = "dbKMS")
public class Acao extends GenericEntity {
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
     * Lista de responsabilidades
     */
    @OneToMany(mappedBy = "acao")
    private Set<Responsabilidade> responsabilidades = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Acao() {
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
     * Obtém a lista de responsabilidades
     *
     * @return Lista de responsabilidades
     */
    public Set<Responsabilidade> getResponsabilidades() {
        return responsabilidades;
    }

    /**
     * Altera a lista de responsabilidades
     *
     * @param responsabilidades Lista de responsabilidades
     */
    public void setResponsabilidades(Set<Responsabilidade> responsabilidades) {
        this.responsabilidades = responsabilidades;
    }

    @Override
    public String toString() {
        return new StringBuilder("Acao{").append("id=").append(id).append(", nome=").append(nome).append('}').toString();
    }

}
