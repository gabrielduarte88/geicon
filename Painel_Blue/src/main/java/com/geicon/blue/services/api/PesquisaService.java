package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;
import java.util.Set;

/**
 * Interface Service - Pesquisa
 *
 * @author Gabriel Duarte
 */
public interface PesquisaService extends Service<Pesquisa> {
    /**
     * Buscar pesquisa por ID
     *
     * @param instituicao Instituição
     * @param id ID
     * @return Pesquisa encontrada
     */
    public Pesquisa buscarPesquisaPorId(Instituicao instituicao, Integer id);

    /**
     * Listar pesquisas por usuário
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     * @return Lista de pesquisas do usuário
     */
    public Set<Pesquisa> listarPesquisasPorUsuario(Instituicao instituicao, Usuario usuario);

    /**
     * Verificar se um usuário é participante de uma pesquisa
     *
     * @param instituicao Instituição
     * @param pesquisa Pesquisa
     * @param usuario Usuário
     * @return true se for participante / false se não
     */
    public boolean isParticipante(Instituicao instituicao, Pesquisa pesquisa, Usuario usuario);

    /**
     * Verificar se um usuário possui uma responsabilidade específica
     *
     * @param instituicao Instituição
     * @param pesquisa Pesquisa
     * @param usuario Usuário
     * @param nomeAcao Nome de ação
     * @return true se possuir / false se não
     */
    public boolean possuiResponsabilidade(Instituicao instituicao, Pesquisa pesquisa, Usuario usuario, String nomeAcao);

    /**
     * Contar pesquisas as quais o usuário é responsável
     *
     * @param instituicao Instituição
     * @param responsavel Usuário
     * @return Quantidade de pesquisas
     */
    public int contarPesquisasPorUsuarioResponsavel(Instituicao instituicao, Usuario responsavel);

    /**
     * Verificar se o usuário é responsável por pesquisas
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     * @return true se for responsável / false se não
     */
    public boolean isResponsavelPesquisas(Instituicao instituicao, Usuario usuario);

    /**
     * Listar pesquisas
     *
     * @param instituicao Instituição
     * @param usuarioAtual Usuário atual
     * @param admin Administrador
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @param itemsPerPage Itens por página
     * @return Resultados da pesquisa
     */
    public SearchResult<Pesquisa> listarPesquisas(Instituicao instituicao, Usuario usuarioAtual, boolean admin, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir pesquisa
     *
     * @param instituicao Instituiçãoj
     * @param pesquisa Pesquisa
     */
    public void inserirPesquisa(Instituicao instituicao, Pesquisa pesquisa);

    /**
     * Excluir pesquisa
     *
     * @param pesquisa Pesquisa
     */
    public void excluirPesquisa(Pesquisa pesquisa);
}
