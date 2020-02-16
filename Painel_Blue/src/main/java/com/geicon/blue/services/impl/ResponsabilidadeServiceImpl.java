package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.ResponsabilidadeDao;
import com.geicon.blue.api.models.Acao;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Responsabilidade;
import com.geicon.blue.api.models.Usuario;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.services.api.AcaoService;
import com.geicon.blue.services.api.ResponsabilidadeService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de ResponsabilidadeService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ResponsabilidadeServiceImpl extends GenericService<Responsabilidade> implements ResponsabilidadeService {
    /**
     * DAO
     */
    @Inject
    protected ResponsabilidadeDao dao;

    @Override
    public ResponsabilidadeDao getDao() {
        return dao;
    }

    @Override
    public Map<Acao, Responsabilidade> mapearResponsabilidadesPorUsuario(Pesquisa pesquisa, Usuario usuario) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("pesquisa", pesquisa));
        c.add(Restrictions.eq("usuario", usuario));
        c.add(Restrictions.isNull("excluido"));

        return list(c).stream().collect(Collectors.toMap(r -> r.getAcao(), r -> r));
    }

    @Override
    public void atualizarResponsabilidades(Pesquisa pesquisa, Usuario usuario, AcaoService acaoService, String acoesId[]) {
        Map<Acao, Responsabilidade> mapResponsabilidades = mapearResponsabilidadesPorUsuario(pesquisa, usuario);

        List<String> lstAcoesId = Arrays.asList(acoesId);

        lstAcoesId.stream()
                .map(a -> Integer.parseInt(a))
                .map(a -> acaoService.buscarAcaoPorId(a))
                .filter(a -> a != null && !mapResponsabilidades.containsKey(a))
                .forEach(a -> {
                    Responsabilidade responsabilidade = new Responsabilidade();
                    responsabilidade.setPesquisa(pesquisa);
                    responsabilidade.setUsuario(usuario);
                    responsabilidade.setAcao(a);
                    responsabilidade.setData(new Date());

                    insert(responsabilidade);
                });

        mapResponsabilidades.keySet()
                .stream()
                .filter(a -> a != null && !lstAcoesId.contains(a.getId().toString()))
                .forEach(a -> {
                    Responsabilidade responsabilidade = mapResponsabilidades.get(a);

                    responsabilidade.setExcluido(new Date());

                    update(responsabilidade);
                });
    }
}
