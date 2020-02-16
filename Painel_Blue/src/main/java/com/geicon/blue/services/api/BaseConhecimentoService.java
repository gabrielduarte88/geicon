package com.geicon.blue.services.api;

import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;
import java.util.Set;

/**
 * Interface Service - BaseConhecimento
 *
 * @author Gabriel Duarte
 */
public interface BaseConhecimentoService extends Service<BaseConhecimento> {
    /**
     * Buscar base de conhecimento pelo ID
     *
     * @param id ID
     * @return Base de conhecimento encontrada
     */
    public BaseConhecimento buscarBasePorId(Integer id);

    /**
     * Listar bases de conhecimento
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
    public SearchResult<BaseConhecimento> listarBases(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento
     */
    public void inserirBaseConhecimento(BaseConhecimento baseConhecimento);

    /**
     * Excluir base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento
     */
    public void excluirBaseConhecimento(BaseConhecimento baseConhecimento);

    /**
     * Listar bases de conhecimento por pesquisa
     *
     * @param pesquisa Pesquisa
     * @return Lista de bases de conhecimento
     */
    public Set<BaseConhecimento> listarBasesPorPesquisa(Pesquisa pesquisa);
}
