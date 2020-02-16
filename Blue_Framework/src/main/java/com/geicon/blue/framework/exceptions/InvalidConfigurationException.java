package com.geicon.blue.framework.exceptions;

/**
 * Exceção para configurações inválidas
 *
 * @author Gabriel Duarte
 */
public class InvalidConfigurationException extends RuntimeException {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor
     */
    public InvalidConfigurationException() {
        super();
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause Causa
     */
    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     * @param cause Causa
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
