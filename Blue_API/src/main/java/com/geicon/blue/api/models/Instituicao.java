package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Instituição
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Instituicao", catalog = "dbKMS")
public class Instituicao extends GenericEntity {
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
     * Domínio
     */
    @Column(name = "dominio", nullable = false, length = 20)
    private String dominio;
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
     * Lista de usuários
     */
    @OneToMany(mappedBy = "instituicao")
    private Set<Usuario> usuarios = new LinkedHashSet<>(0);
    /**
     * Lista de pesquisas
     */
    @OneToMany(mappedBy = "instituicao")
    private Set<Pesquisa> pesquisas = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Instituicao() {
    }

    /**
     * Obtém o nome
     *
     * @return Nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o nome
     *
     * @param nome Nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o domínio
     *
     * @return Domínio
     */
    public String getDominio() {
        return dominio;
    }

    /**
     * Altera o domínio
     *
     * @param dominio Domínio
     */
    public void setDominio(String dominio) {
        this.dominio = dominio;
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
     * Obtém a data de exclusão
     *
     * @return Data de exclusão
     */
    public Date getExcluido() {
        return excluido;
    }

    /**
     * Altera a data de exclusão
     *
     * @param excluido Data de exclusão
     */
    public void setExcluido(Date excluido) {
        this.excluido = excluido;
    }

    /**
     * Obtém a lista de usuários
     *
     * @return Lista de usuários
     */
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Altera a lista de usuários
     *
     * @param usuarios
     */
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Obtém a lista de pesquisas
     *
     * @return Lista de pesquisas
     */
    public Set<Pesquisa> getPesquisas() {
        return pesquisas;
    }

    /**
     * Altera a lista de pesquisas
     *
     * @param pesquisas Lista de pesquisas
     */
    public void setPesquisas(Set<Pesquisa> pesquisas) {
        this.pesquisas = pesquisas;
    }

    @Override
    public String toString() {
        return new StringBuilder("Instituicao{").append("id=").append(id).append(", nome=").append(nome).append(", dominio=").append(dominio).append(", data=").append(data).append(", excluido=").append(excluido).append('}').toString();
    }

}
