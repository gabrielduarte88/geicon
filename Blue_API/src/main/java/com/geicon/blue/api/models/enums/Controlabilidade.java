package com.geicon.blue.api.models.enums;

/**
 * Controlabilidade
 *
 * @author Gabriel Duarte
 */
public enum Controlabilidade {
    /**
     * Controlável
     */
    CT("Controlável"),
    /**
     * Penumbra
     */
    PN("Penumbra"),
    /**
     * Não-controlável
     */
    NC("Não-controlável");

    /**
     * Descrição
     */
    private final String desc;

    /**
     * Construtor
     *
     * @param desc Descrição
     */
    private Controlabilidade(String desc) {
        this.desc = desc;
    }

    /**
     * Obtém a descrição
     *
     * @return Descrição
     */
    public String getDesc() {
        return desc;
    }
}
