package com.geicon.blue.framework.validation;

import java.util.Set;

/**
 * Validador
 *
 * @author Gabriel Duarte
 */
public interface Validator {
    /**
     * Adiciona um item na lista de verificação
     *
     * @param item Item de validação
     */
    public void add(ValidationItem item);

    /**
     * Adiciona um item na lista de verificação
     *
     * @param valid Validade do item
     * @param message Mensagem de erro
     */
    public void add(Boolean valid, String message);

    /**
     * Adiciona um item na lista de verificação
     *
     * @param value String a ser verificado
     * @param message Mensagem de erro
     */
    public void add(String value, String message);

    /**
     * Adiciona um item na lista de verificação
     *
     * @param value Objeto a ser verificado
     * @param message Mensagem de erro
     */
    public void add(Object value, String message);

    /**
     * Obtém os itens do validador
     *
     * @return Itens do validador
     */
    public Set<ValidationItem> getItems();

    /**
     * Mescla as verificações de 2 validadores
     *
     * @param validator Validador a ser mesclado
     */
    public void merge(Validator validator);

    /**
     * Verifica se existem itens com erro
     *
     * @return true se existire, false se não
     */
    public boolean hasErrors();

    /**
     * Retorna as mensagens de erro encontradas
     *
     * @return Mensagens de erro
     */
    public Set<String> getErrors();
}
