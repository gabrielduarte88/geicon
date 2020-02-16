package com.geicon.blue.framework.exceptions;

/**
 * Exceção para erros internos do sistema
 *
 * @author Gabriel Duarte
 */
public class InternalException extends RuntimeException {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor
     */
    public InternalException() {
        super();
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     */
    public InternalException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause Causa
     */
    public InternalException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     * @param cause Causa
     */
    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

}
