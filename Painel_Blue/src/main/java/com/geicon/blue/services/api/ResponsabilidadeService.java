package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Acao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Responsabilidade;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.persistence.services.Service;
import java.util.Map;

/**
 * Interface Service - Responsabilidade
 *
 * @author Gabriel Duarte
 */
public interface ResponsabilidadeService extends Service<Responsabilidade> {
    /**
     * Mapear responsabilidades por usuário
     *
     * @param pesquisa Pesquisa
     * @param usuario Usuário
     * @return Mapa de responsabilidades
     */
    public Map<Acao, Responsabilidade> mapearResponsabilidadesPorUsuario(Pesquisa pesquisa, Usuario usuario);

    /**
     * Atualizar responsabilidades dos usuários
     *
     * @param pesquisa Pesquisa
     * @param usuario Usuário
     * @param acaoService Serviços de ação
     * @param acoesId IDs das ações selecionadas
     */
    public void atualizarResponsabilidades(Pesquisa pesquisa, Usuario usuario, AcaoService acaoService, String acoesId[]);
}
