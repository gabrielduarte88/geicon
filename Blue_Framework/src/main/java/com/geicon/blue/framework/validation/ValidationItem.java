package com.geicon.blue.framework.validation;

/**
 * Item da validação
 *
 * @author Gabriel Duarte
 */
public interface ValidationItem {
    /**
     * Obtém a mensagem de erro atual
     *
     * @return Mensagem de erro
     */
    public String getMessage();

    /**
     * Verifica se o item é valido
     *
     * @return true se o item for válido, false se não
     */
    public boolean isValid();
}
