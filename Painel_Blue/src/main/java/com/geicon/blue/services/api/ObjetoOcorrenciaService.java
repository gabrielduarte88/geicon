package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Set;

/**
 * Interface Service - ObjetoOcorrencia
 *
 * @author Gabriel Duarte
 */
public interface ObjetoOcorrenciaService extends Service<ObjetoOcorrencia> {
    /**
     * Verificar existência de ocorrência de objeto
     *
     * @param objeto Objeto
     * @param ocorrencia Ocorrência
     * @return true se existir / false se não
     */
    public boolean verificarOcorrenciaPorPosicao(Objeto objeto, ObjetoOcorrencia ocorrencia);

    /**
     * Inserir ocorrência
     *
     * @param ocorrencia Ocorrência
     */
    public void inserirOcorrencia(ObjetoOcorrencia ocorrencia);

    /**
     * Listar ocorrências por objeto
     *
     * @param objeto Objeto
     * @return Lista de ocorrências
     */
    public Set<ObjetoOcorrencia> listarOcorrenciasPorObjeto(Objeto objeto);

    /**
     * Contar ocorrências por documento
     *
     * @param documento Documento
     * @return Quantidade de ocorrênicas
     */
    public int contarOcorrenciasPorDocumento(Documento documento);
}
