package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;

/**
 * Interface Service - Elicitacao
 *
 * @author Gabriel Duarte
 */
public interface ElicitacaoService extends Service<Elicitacao> {
    /**
     * Buscar elicitação por ID
     *
     * @param id ID
     * @return Elicitação encontrada
     */
    public Elicitacao buscarElicitacaoPorId(Integer id);

    /**
     * Listar elicitações
     *
     * @param pesquisa Pesquisa
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @param itemsPerPage Itens por página
     * @return Resultados da pesquisa
     */
    public SearchResult<Elicitacao> listarElicitacoes(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir elicitação
     *
     * @param elicitacao Elicitação
     */
    public void inserirElicitacao(Elicitacao elicitacao);

    /**
     * Excluir elicitação
     *
     * @param elicitacao Elicitação
     */
    public void excluirElicitacao(Elicitacao elicitacao);

    /**
     * Calcular score da elicitação
     *
     * @param elicitacao Elicitação
     * @return Score obtido
     */
    public Float calcularScore(Elicitacao elicitacao);

    /**
     * Calcular score2 da elicitação
     *
     * @param elicitacao Elicitação
     * @return Score obtido
     */
    public Float calcularScore2(Elicitacao elicitacao);

    public void alterarElicitacao(Elicitacao elicitacao);
}
