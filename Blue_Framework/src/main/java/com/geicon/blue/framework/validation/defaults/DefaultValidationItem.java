package com.geicon.blue.framework.validation.defaults;

import com.geicon.blue.framework.validation.ValidationItem;

/**
 * Item da validação
 *
 * @author Gabriel Duarte
 */
public class DefaultValidationItem implements ValidationItem {
    /**
     * Validade do item
     */
    private final boolean valid;
    /**
     * Mensagem de erro
     */
    private final String message;

    /**
     * Construtor
     *
     * @param valid Validade do item
     * @param message Mensagem de erro
     */
    public DefaultValidationItem(Boolean valid, String message) {
        this.valid = valid != null && valid;
        this.message = message;
    }

    /**
     * Construtor
     *
     * @param value String a ser verificado
     * @param message Mensagem de erro
     */
    public DefaultValidationItem(String value, String message) {
        this.valid = value != null && !value.isEmpty();
        this.message = message;
    }

    /**
     * Construtor
     *
     * @param value Objeto a ser verificado
     * @param message Mensagem de erro
     */
    public DefaultValidationItem(Object value, String message) {
        this.valid = value != null;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}
