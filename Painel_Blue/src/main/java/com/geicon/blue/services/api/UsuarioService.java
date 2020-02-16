package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.persistence.services.Service;
import com.geicon.blue.framework.util.SearchResult;
import java.util.Set;

/**
 * Interface Service - Usuario
 *
 * @author Gabriel Duarte
 */
public interface UsuarioService extends Service<Usuario> {
    /**
     * Buscar usuário por ID
     *
     * @param instituicao Instituição
     * @param id ID
     * @return Usuário encontrado
     */
    public Usuario buscarUsuarioPorId(Instituicao instituicao, Integer id);

    /**
     * Autenticação do usuário
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     * @return Usuário autenticado
     */
    public Usuario autenticarUsuario(Instituicao instituicao, Usuario usuario);

    /**
     * Autenticação do usuário por e-mail e celular
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     * @return Usuário autenticado
     */
    public Usuario autenticarUsuarioPorEmailECelular(Instituicao instituicao, Usuario usuario);

    /**
     * Gerar uma nova senha para o usuário
     *
     * @param usuario Usuário
     * @return Senha gerada
     */
    public String gerarNovaSenhaUsuario(Usuario usuario);

    /**
     * Verifica se existe um usuário com os mesmos dados chave
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     * @param alteracao indicador de inserção ou alteração
     * @return true se existir / false se não
     */
    public boolean verificarUsuarioDuplicado(Instituicao instituicao, Usuario usuario, boolean alteracao);

    /**
     * Verificar se o usuário é um administrador do sistema
     *
     * @param usuario Usuário
     * @return true se for administrador / false se não
     */
    public boolean isAdministrador(Usuario usuario);

    /**
     * Listar usuários
     *
     * @param instituicao Instituição
     * @param filterField Campo do filtro
     * @param filterValue Valor do filtro
     * @param filterType Tipo do filtro
     * @param orderField Campo de ordenação
     * @param order Ordenação
     * @param page Página
     * @param itemsPerPage Itens por página
     * @return Resultados da pesquisa
     */
    public SearchResult<Usuario> listarUsuarios(Instituicao instituicao, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage);

    /**
     * Inserir usuário
     *
     * @param instituicao Instituição
     * @param usuario Usuário
     */
    public void inserirUsuario(Instituicao instituicao, Usuario usuario);

    /**
     * Excluir usuário
     *
     * @param usuario Usuário
     */
    public void excluirUsuario(Usuario usuario);

    /**
     * Lista os usuários ordenados por nome
     *
     * @param instituicao Instituição
     * @return Lista de usuários
     */
    public Set<Usuario> listarUsuariosOrdenadosPorNome(Instituicao instituicao);
}
