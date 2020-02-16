package com.geicon.blue.api.models.enums;

/**
 * Peso da relação
 *
 * @author Gabriel Duarte
 */
public enum RelacaoPeso {
    /**
     * Reforço
     */
    R("Reforço"),
    /**
     * Balanceamento
     */
    B("Balanceamento");

    /**
     * Descrição
     */
    private final String desc;

    /**
     * Construtor
     *
     * @param desc Descrição
     */
    private RelacaoPeso(String desc) {
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
