package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Agente;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;
import java.util.Set;

/**
 * Interface Service - Documento
 *
 * @author Gabriel Duarte
 */
public interface DocumentoService extends Service<Documento> {
    /**
     * Buscar documento por ID
     *
     * @param id ID
     * @return Documento encontrado
     */
    public Documento buscarDocumentoPorId(Integer id);

    /**
     * Listar documentos
     *
     * @param agente Agente
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @param itemsPerPage Itens por página
     * @return Resultados da pesquisa
     */
    public SearchResult<Documento> listarDocumentos(Agente agente, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir documento
     *
     * @param documento Documento
     */
    public void inserirDocumento(Documento documento);

    /**
     * Excluir documento
     *
     * @param documento Documento
     */
    public void excluirDocumento(Documento documento);

    /**
     * Contar documentos por agente
     *
     * @param agente Agente
     * @return Quantidade de documentos por agente
     */
    public int contarDocumentosPorAgente(Agente agente);

    /**
     * Listar documentos por agente
     *
     * @param agente Agente
     * @return Documentos por agente
     */
    public Set<Documento> listarDocumentosPorAgente(Agente agente);
}
