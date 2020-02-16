package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.BaseConhecimento;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;
import java.util.Set;

/**
 * Interface Service - Agente
 *
 * @author Gabriel Duarte
 */
public interface AgenteService extends Service<Agente> {
    /**
     * Buscar agente por ID
     *
     * @param id ID
     * @return Agente encontrado
     */
    public Agente buscarAgentePorId(Integer id);

    /**
     * Listar agentes
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
    public SearchResult<Agente> listarAgentes(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir agente
     *
     * @param agente Agente
     */
    public void inserirAgente(Agente agente);

    /**
     * Excluir agente
     *
     * @param agente Agente
     */
    public void excluirAgente(Agente agente);

    /**
     * Listar agentes por base de conhecimento
     *
     * @param baseConhecimento Base de conhecimento
     * @return Lista de agentes
     */
    public Set<Agente> listarAgentesPorBaseDeConhecimento(BaseConhecimento baseConhecimento);
}
