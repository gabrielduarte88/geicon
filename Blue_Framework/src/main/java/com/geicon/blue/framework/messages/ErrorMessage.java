package com.geicon.blue.framework.messages;

import java.util.Arrays;
import java.util.Collection;

/**
 * Mensagem de erro
 *
 * @author Gabriel Duarte
 */
public final class ErrorMessage extends Message {
    /**
     * Construtor
     *
     * @param message texto da mensagem
     */
    public ErrorMessage(final String message) {
        super(message, null);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public ErrorMessage(final String message, final Collection<?> extra) {
        super(message, extra);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public ErrorMessage(final String message, final Object... extra) {
        super(message, Arrays.asList(extra));
    }
}
