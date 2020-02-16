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
 * Usuário
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Usuario", catalog = "dbKMS")
public class Usuario extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Responsável
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicao", nullable = false)
    private Instituicao instituicao;
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
     * Celular
     */
    @Column(name = "celular", length = 20)
    private String celular;
    /**
     * Senha
     */
    @Column(name = "senha", nullable = false, length = 64)
    private String senha;
    /**
     * Administrador
     */
    @Column(name = "administrador")
    private Boolean administrador;
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
     * Lista de pesquisas
     */
    @OneToMany(mappedBy = "responsavel")
    private Set<Pesquisa> pesquisas = new LinkedHashSet<>(0);
    /**
     * Lista de responsabilidades
     */
    @OneToMany(mappedBy = "usuario")
    private Set<Responsabilidade> responsabilidades = new LinkedHashSet<>(0);
    /**
     * Lista de logs
     */
    @OneToMany(mappedBy = "usuario")
    private Set<Log> logs = new LinkedHashSet<>(0);
    /**
     * Lista de elicitações
     */
    @OneToMany(mappedBy = "responsavel")
    private Set<Elicitacao> elicitacoes = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Usuario() {
    }

    /**
     * Obtém a instiuição
     *
     * @return Instituição
     */
    public Instituicao getInstituicao() {
        return instituicao;
    }

    /**
     * Altera a instituição
     *
     * @param instituicao Instituição
     */
    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
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
     * Obtém e-mail
     *
     * @return E-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Altera e-mail
     *
     * @param email E-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém celular
     *
     * @return Celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * Altera celular
     *
     * @param celular Celular
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * Obtém senha
     *
     * @return Senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Altera senha
     *
     * @param senha Senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Obtém administrador
     *
     * @return Administrador
     */
    public Boolean getAdministrador() {
        return administrador;
    }

    /**
     * Altera administrador
     *
     * @param administrador Administrador
     */
    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
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

    /**
     * Obtém a lista de logs
     *
     * @return Lista de logs
     */
    public Set<Log> getLogs() {
        return logs;
    }

    /**
     * Altera a lista de logs
     *
     * @param logs Lista de logs
     */
    public void setLogs(Set<Log> logs) {
        this.logs = logs;
    }

    /**
     * Obtém a lista de elicitações
     *
     * @return Lista de elicitações
     */
    public Set<Elicitacao> getElicitacoes() {
        return elicitacoes;
    }

    /**
     * Altera a lista de elicitações
     *
     * @param elicitacoes Lista de elicitações
     */
    public void setElicitacoes(Set<Elicitacao> elicitacoes) {
        this.elicitacoes = elicitacoes;
    }

    @Override
    public String toString() {
        return new StringBuilder("Usuario{").append("id=").append(id).append(", instituicao=").append(instituicao).append(", nome=").append(nome).append(", email=").append(email).append(", celular=").append(celular).append(", senha=").append(senha).append(", administrador=").append(administrador).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
