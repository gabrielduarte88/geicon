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
 * Pesquisa
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "Pesquisa", catalog = "dbKMS")
public class Pesquisa extends GenericEntity {
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
     * Responsável
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel", nullable = false)
    private Usuario responsavel;
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
     * Lista de responsabilidades
     */
    @OneToMany(mappedBy = "pesquisa")
    private Set<Responsabilidade> responsabilidades = new LinkedHashSet<>(0);
    /**
     * Lista de bases de conhecimento
     */
    @OneToMany(mappedBy = "pesquisa")
    private Set<BaseConhecimento> basesConhecimento = new LinkedHashSet<>(0);

    /**
     * Construtor
     */
    public Pesquisa() {
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
     * Obtém responsável
     *
     * @return Responsável
     */
    public Usuario getResponsavel() {
        return responsavel;
    }

    /**
     * Altera responsável
     *
     * @param responsavel Responsável
     */
    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
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
     * Obtém a lista de bases de conhecimento
     *
     * @return Lista de base de conhecimento
     */
    public Set<BaseConhecimento> getBasesConhecimento() {
        return basesConhecimento;
    }

    /**
     * Altera a lista de bases de conhecimento
     *
     * @param basesConhecimento Lista de bases de conhecimento
     */
    public void setBaseConhecimentos(Set<BaseConhecimento> basesConhecimento) {
        this.basesConhecimento = basesConhecimento;
    }

    @Override
    public String toString() {
        return new StringBuilder("Pesquisa{").append("id=").append(id).append(", instituicao=").append(instituicao).append(", nome=").append(nome).append(", responsavel=").append(responsavel).append(", data=").append(data).append(", excluido=").append(excluido).append("}").toString();
    }
}
