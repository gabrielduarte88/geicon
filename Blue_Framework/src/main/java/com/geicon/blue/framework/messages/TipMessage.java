package com.geicon.blue.framework.messages;

import java.util.Arrays;
import java.util.Collection;

/**
 * Dica
 *
 * @author Gabriel Duarte
 */
public final class TipMessage extends Message {
    /**
     * Construtor
     *
     * @param message texto da mensagem
     */
    public TipMessage(final String message) {
        super(message, null);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public TipMessage(final String message, final Collection<?> extra) {
        super(message, extra);
    }

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     */
    public TipMessage(final String message, final Object... extra) {
        super(message, Arrays.asList(extra));
    }
}
