package com.geicon.blue.api.models.enums;

/**
 * Status da tarefa
 *
 * @author Gabriel Duarte
 */
public enum TarefaStatus {
    /**
     * Em espera
     */
    EM_ESPERA("Em espera"),
    /**
     * Em execu��o
     */
    EM_EXECUCAO("Em execução"),
    /**
     * Sucesso
     */
    SUCESSO("Sucesso"),
    /**
     * Erro
     */
    ERRO("Erro"),
    /**
     * Cancelada
     */
    CANCELADA("Cancelada");

    /**
     * Descrição
     */
    private final String desc;

    /**
     * Construtor
     *
     * @param desc Descrição
     */
    private TarefaStatus(String desc) {
        this.desc = desc;
    }

    /**
     * Obtém o nome do status
     *
     * @return Nome do status
     */
    public String getName() {
        return toString();
    }

    /**
     * Obtém a descrição do status
     *
     * @return Descrição do status
     */
    public String getDesc() {
        return this.desc;
    }
}
