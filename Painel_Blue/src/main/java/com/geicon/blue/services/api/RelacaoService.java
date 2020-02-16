package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Set;

/**
 * Interface Service - Relacao
 *
 * @author Gabriel Duarte
 */
public interface RelacaoService extends Service<Relacao> {
    /**
     * Buscar relação por ID
     *
     * @param id ID
     * @return Relação encontrada
     */
    public Relacao buscarRelacaoPorId(Integer id);

    /**
     * Verificar se uma relação existe na elicitação, documento e posições
     * informadas
     *
     * @param relacao Relação verificada
     * @return true se a relação existir / false se não
     */
    public boolean verificarRelacaoExistentePorPosicao(Relacao relacao);

    /**
     * Inserir relação
     *
     * @param relacao Relação
     */
    public void inserirRelacao(Relacao relacao);

    /**
     * Excluir relação
     *
     * @param relacao Relação
     */
    public void excluirRelacao(Relacao relacao);

    /**
     * Listar elicitações por elicitação
     *
     * @param elicitacao Elicitação
     * @return Lista de relações
     */
    public Set<Relacao> listarRelacoesPorElicitacao(Elicitacao elicitacao);

    /**
     * Contar relações por documento
     *
     * @param documento Documento
     * @return Quantidade de relações
     */
    public int contarRelacoesPorDocumento(Documento documento);

    /**
     * Buscar relações por objeto de origem
     *
     * @param objeto Objeto de origem
     * @return Relações encontradas
     */
    public Set<Relacao> buscarRelacoesPorOrigem(Objeto objeto);

    /**
     * Buscar relações por objeto de origem
     *
     * @param objeto Objeto de origem
     * @return Relações encontradas
     */
    public Set<Relacao> buscarRelacoesPorDestino(Objeto objeto);
}
