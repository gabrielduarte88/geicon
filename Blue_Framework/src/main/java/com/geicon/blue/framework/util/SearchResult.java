package com.geicon.blue.framework.util;

import java.util.Set;

/**
 * Resultado de pesquisa
 *
 * @author Gabriel Duarte
 * @param <T> Tipo do resultado
 */
public class SearchResult<T extends Object> {
    /**
     * Lista de itens da pesquisa
     */
    private final Set<T> items;
    /**
     * Quantidade de itens
     */
    private final int total;

    /**
     * Construtor
     *
     * @param items Lista de itens da pesquisa
     * @param total Quantidade de itens
     */
    public SearchResult(Set<T> items, int total) {
        this.items = items;
        this.total = total;
    }

    /**
     * Obtém a lista de itens
     *
     * @return Lista de itens
     */
    public Set<T> getItems() {
        return items;
    }

    /**
     * Obtém a quantidade de itens
     *
     * @return Quantidade de itens
     */
    public int getTotal() {
        return total;
    }

}
