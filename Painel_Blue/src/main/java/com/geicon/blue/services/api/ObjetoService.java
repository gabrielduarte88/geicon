package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Set;

/**
 * Interface Service - Objeto
 *
 * @author Gabriel Duarte
 */
public interface ObjetoService extends Service<Objeto> {
    /**
     * Buscar objeto por ID
     *
     * @param id ID
     * @return Objeto encontrado
     */
    public Objeto buscarObjetoPorId(Integer id);

    /**
     * Buscar objeto por posição
     *
     * @param elicitacao Elicitação
     * @param ocorrencia Ocorrênica
     * @return Objeto encontrado
     */
    public Objeto buscarObjetoPorPosicao(Elicitacao elicitacao, ObjetoOcorrencia ocorrencia);

    /**
     * Inserir objeto
     *
     * @param objeto Objeto
     */
    public void inserirObjeto(Objeto objeto);

    /**
     * Excluir objeto
     *
     * @param objeto Objeto
     */
    public void excluirObjeto(Objeto objeto);

    /**
     * Listar objetos por elicitação
     *
     * @param elicitacao Elicitação
     * @return Lista de objetos
     */
    public Set<Objeto> listarObjetosPorElicitacao(Elicitacao elicitacao);
}
