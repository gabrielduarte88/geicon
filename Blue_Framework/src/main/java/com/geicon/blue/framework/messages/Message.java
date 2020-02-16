package com.geicon.blue.framework.messages;

import com.geicon.blue.framework.exceptions.IllegalParametersException;
import java.util.Collection;
import java.util.Collections;

/**
 * Mensagens do sistema
 *
 * @author Gabriel Duarte
 */
public abstract class Message {
    /**
     * Texto da mensagem
     */
    protected final String message;
    /**
     * Tipo da mensagem
     */
    protected final String type;
    /**
     * Elementos extras
     */
    protected final Collection<?> extra;

    /**
     * Construtor
     *
     * @param message texto da mensagem
     * @param extra Elementos extras
     * @throws com.geicon.blue.framework.exceptions.IllegalParametersException
     */
    protected Message(final String message, final Collection<?> extra) {
        if (message == null) {
            throw new IllegalParametersException("Missing message.");
        }

        this.message = message;
        this.type = this.getClass().getSimpleName().replace("Message", "").toLowerCase();
        this.extra = extra;
    }

    /**
     * Obtém a mensagem
     *
     * @return mensagem
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Obtém o tipo
     *
     * @return Tipo
     */
    public final String getType() {
        return this.getClass().getSimpleName().replace("Message", "").toLowerCase();
    }

    /**
     * Obtém o extra
     *
     * @return Extra
     */
    public final Collection<?> getExtra() {
        return Collections.unmodifiableCollection(extra);
    }

    /**
     * Transcreve o objeto em String
     *
     * @return String representando o objeto
     */
    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName()).append("{message=").append(message).append(", extra=").append(extra).append('}').toString();
    }
}
