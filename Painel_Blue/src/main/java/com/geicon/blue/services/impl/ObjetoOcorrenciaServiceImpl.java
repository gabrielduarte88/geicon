package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.ObjetoOcorrenciaDao;
import com.geicon.blue.api.models.Documento;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.ObjetoOcorrenciaService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de ObjetoOcorrenciaService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ObjetoOcorrenciaServiceImpl extends GenericService<ObjetoOcorrencia> implements ObjetoOcorrenciaService {
    /**
     * DAO
     */
    @Inject
    protected ObjetoOcorrenciaDao dao;

    @Override
    public ObjetoOcorrenciaDao getDao() {
        return dao;
    }

    @Override
    public boolean verificarOcorrenciaPorPosicao(Objeto objeto, ObjetoOcorrencia ocorrencia) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("objeto", objeto));
        c.add(Restrictions.eq("posicaoInicial", ocorrencia.getPosicaoInicial()));
        c.add(Restrictions.eq("posicaoFinal", ocorrencia.getPosicaoFinal()));
        c.add(Restrictions.isNull("excluido"));

        return count(c) > 0;
    }

    @Override
    public void inserirOcorrencia(ObjetoOcorrencia ocorrencia) {
        ocorrencia.setData(new Date());

        insert(ocorrencia);
    }

    @Override
    public Set<ObjetoOcorrencia> listarOcorrenciasPorObjeto(Objeto objeto) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("objeto", objeto));
        c.add(Restrictions.isNull("excluido"));

        return list(c);
    }

    @Override
    public int contarOcorrenciasPorDocumento(Documento documento) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("documento", documento));
        c.add(Restrictions.isNull("excluido"));

        return count(c);
    }
}
