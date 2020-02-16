package com.geicon.blue.framework.exceptions;

/**
 * Exceção para parâmetros inválidos
 *
 * @author Gabriel Duarte
 */
public class EmailException extends Exception {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor
     */
    public EmailException() {
        super();
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     */
    public EmailException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause Causa
     */
    public EmailException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     * @param cause Causa
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
