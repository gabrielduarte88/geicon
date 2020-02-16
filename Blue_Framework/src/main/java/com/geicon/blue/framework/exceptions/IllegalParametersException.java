package com.geicon.blue.framework.exceptions;

/**
 * Exceção para parâmetros inválidos
 *
 * @author Gabriel Duarte
 */
public class IllegalParametersException extends RuntimeException {
    /**
     * UID Serial
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor
     */
    public IllegalParametersException() {
        super();
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     */
    public IllegalParametersException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause Causa
     */
    public IllegalParametersException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message Mensagem
     * @param cause Causa
     */
    public IllegalParametersException(String message, Throwable cause) {
        super(message, cause);
    }

}
