package com.geicon.blue.framework.persistence.models;

/**
 * Entidade do sistema
 *
 * @author Gabriel Duarte
 */
public interface Entity extends java.io.Serializable {
    /**
     * Obtém o ID
     *
     * @return ID da entidade
     */
    public Integer getId();

    /**
     * Altera o ID da entidade
     *
     * @param id Novo ID
     */
    public void setId(Integer id);

    /**
     * Obtém o ID codificado da entidade
     *
     * @return ID codificado
     */
    public String getCode();

    /**
     * Altera o ID através do código
     *
     * @param code Código
     */
    public void setCode(String code);

    /**
     * Gerar hashcode do objeto
     *
     * @return hashcode
     */
    @Override
    public int hashCode();

    /**
     * Comparar objetos
     *
     * @param obj Objeto a ser comparado
     * @return true se o objeto for igual / false se n�o
     */
    @Override
    public boolean equals(Object obj);
}
