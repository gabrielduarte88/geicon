package com.geicon.blue.framework.messages;

import java.util.Arrays;
import java.util.Collection;

/**
 * Mensagem de sucesso
 *
 * @author Gabriel Duarte
 */
public final class SuccessMessage extends Message {
    /**
     * Construtor
     *
     * @param message texto da mensagem
     */
    public SuccessMessage(final String message) {
        super(message, null);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public SuccessMessage(final String message, final Collection<?> extra) {
        super(message, extra);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public SuccessMessage(final String message, final Object... extra) {
        super(message, Arrays.asList(extra));
    }
}
