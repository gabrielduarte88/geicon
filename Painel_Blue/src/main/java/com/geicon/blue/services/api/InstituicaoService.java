package com.geicon.blue.services.api;

import com.geicon.blue.api.models.Instituicao;
import com.geicon.blue.framework.persistence.services.Service;

/**
 * Interface Service - Instituicao
 *
 * @author Gabriel Duarte
 */
public interface InstituicaoService extends Service<Instituicao> {
    /**
     * Buscar instituição por ID
     *
     * @param id ID
     * @return Instituição encontrada
     */
    public Instituicao buscarInstituicaoPorId(Integer id);

    /**
     * Buscar instituição por domínio
     *
     * @param dominio Domínio
     * @return Instituição encontrada
     */
    public Instituicao buscarInstituicaoPorDominio(String dominio);
}
