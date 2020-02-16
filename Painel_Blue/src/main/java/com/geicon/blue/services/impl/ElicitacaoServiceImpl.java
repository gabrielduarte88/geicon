package com.geicon.blue.services.impl;

import com.geicon.blue.api.dao.api.ElicitacaoDao;
import com.geicon.blue.api.models.Elicitacao;
import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.Pesquisa;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.api.models.enums.RelacaoPeso;
import com.geicon.blue.framework.exceptions.InternalException;
import com.geicon.blue.framework.persistence.services.GenericService;
import com.geicon.blue.framework.util.SearchResult;
import com.geicon.blue.services.api.ElicitacaoService;
import com.geicon.blue.services.api.ObjetoService;
import com.geicon.blue.services.api.RelacaoService;
import java.util.Date;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Implementação de ElicitacaoService
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class ElicitacaoServiceImpl extends GenericService<Elicitacao> implements ElicitacaoService {
    /**
     * DAO
     */
    @Inject
    protected ElicitacaoDao dao;

    /**
     * Serviços de objeto
     */
    @Inject
    protected ObjetoService objetoService;

    /**
     * Serviços de relação
     */
    @Inject
    protected RelacaoService relacaoService;

    @Override
    public ElicitacaoDao getDao() {
        return dao;
    }

    @Override
    public Elicitacao buscarElicitacaoPorId(Integer id) {
        Criteria c = createCriteria();

        c.add(Restrictions.eq("id", id));
        c.add(Restrictions.isNull("excluido"));

        return load(c);
    }

    @Override
    public SearchResult<Elicitacao> listarElicitacoes(Pesquisa pesquisa, String filterField, String filterValue, String filterType, String orderField, String order, Integer page, Integer itemsPerPage) {
        try {
            Criteria c = createCriteria();
            Criteria cCount = createCriteria();

            c.createAlias("base", "base");
            cCount.createAlias("base", "base");

            c = parseFilter(c, filterType, filterField, filterValue);
            cCount = parseFilter(cCount, filterType, filterField, filterValue);

            c.add(Restrictions.eq("base.pesquisa", pesquisa));
            cCount.add(Restrictions.eq("base.pesquisa", pesquisa));

            c.add(Restrictions.isNull("excluido"));
            cCount.add(Restrictions.isNull("excluido"));

            if (order.equals("ASC")) {
                c.addOrder(Order.asc(orderField));
            }
            else {
                c.addOrder(Order.desc(orderField));
            }

            c.setFirstResult(page * itemsPerPage);
            c.setMaxResults(itemsPerPage);

            return new SearchResult<>(list(c), count(cCount));
        }
        catch (Exception ex) {
            throw new InternalException(ex);
        }
    }

    @Override
    public void inserirElicitacao(Elicitacao elicitacao) {
        elicitacao.setData(new Date());

        insert(elicitacao);
    }

    @Override
    public void excluirElicitacao(Elicitacao elicitacao) {
        elicitacao.setExcluido(new Date());

        update(elicitacao);
    }

    @Override
    public Float calcularScore(Elicitacao elicitacao) {
        Float score = 0f;

        for (Objeto objeto : objetoService.listarObjetosPorElicitacao(elicitacao)) {
            Float scoreObjeto = 0f;

            //Saída
            Set<Relacao> relacoes = relacaoService.buscarRelacoesPorOrigem(objeto);

            int qtdeR = getQtdeR(relacoes);
            int qtdeB = getQtdeB(relacoes);
//
            if (qtdeR > 0 || qtdeB > 0) {
                //Somente R
                if (qtdeB == 0 && qtdeR > 0) {
                    scoreObjeto += getValorObjeto(objeto) + getSomaR(relacoes);
                }
                //Somente B
                else if (qtdeR == 0 && qtdeB > 0) {
                    scoreObjeto += (getValorObjeto(objeto) + getSomaB(relacoes)) / (qtdeB + 1);
                }
                else if (getSomaR(relacoes) > getSomaB(relacoes)) {
                    scoreObjeto = getValorObjeto(objeto) + getSomaR(relacoes) - getSomaB(relacoes);
                }
                else if (getSomaR(relacoes) < getSomaB(relacoes)) {
                    scoreObjeto = getSomaB(relacoes);
                }
                else {
                    scoreObjeto = getValorObjeto(objeto);
                }
            }

            score += scoreObjeto;
        }

        for (Objeto objeto : objetoService.listarObjetosPorElicitacao(elicitacao)) {
            float scoreObjeto = 0;

            //Chegada
            Set<Relacao> relacoesChegada = relacaoService.buscarRelacoesPorDestino(objeto);

            int qtdeR = getQtdeR(relacoesChegada);
            int qtdeB = getQtdeB(relacoesChegada);

            if (qtdeR > 0 || qtdeB > 0) {
                //Somente R
                if (qtdeB == 0 && qtdeR > 0) {
                    scoreObjeto += getValorObjeto(objeto) + getSomaR(relacoesChegada);
                }
                //Somente B
                else if (qtdeR == 0 && qtdeB > 0) {
                    scoreObjeto += (getValorObjeto(objeto) + getSomaB(relacoesChegada)) / (qtdeB + 1);
                }
                else if (getSomaR(relacoesChegada) > getSomaB(relacoesChegada)) {
                    scoreObjeto = getValorObjeto(objeto) + getSomaR(relacoesChegada) - getSomaB(relacoesChegada);
                }
                else if (getSomaR(relacoesChegada) < getSomaB(relacoesChegada)) {
                    scoreObjeto = getSomaB(relacoesChegada);
                }
                else {
                    scoreObjeto = getValorObjeto(objeto);
                }
            }

            score += scoreObjeto;
        }

        return score;
    }

    @Override
    public Float calcularScore2(Elicitacao elicitacao) {
        Float score = 0f;

        for (Objeto objeto : objetoService.listarObjetosPorElicitacao(elicitacao)) {
            Float scoreObjeto = 0f;

            //Saída
            Set<Relacao> relacoes = relacaoService.buscarRelacoesPorOrigem(objeto);

            int qtdeR = getQtdeR(relacoes);
            int qtdeB = getQtdeB(relacoes);
//
            if (qtdeR > 0 || qtdeB > 0) {
                //Somente R
                if (qtdeB == 0 && qtdeR > 0) {
                    scoreObjeto += getValorObjeto2(objeto) + getSomaR(relacoes);
                }
                //Somente B
                else if (qtdeR == 0 && qtdeB > 0) {
                    scoreObjeto += (getValorObjeto2(objeto) + getSomaB(relacoes)) / (qtdeB + 1);
                }
                else if (getSomaR(relacoes) > getSomaB(relacoes)) {
                    scoreObjeto = getValorObjeto2(objeto) + getSomaR(relacoes) - getSomaB(relacoes);
                }
                else if (getSomaR(relacoes) < getSomaB(relacoes)) {
                    scoreObjeto = getSomaB(relacoes);
                }
                else {
                    scoreObjeto = getValorObjeto2(objeto);
                }
            }

            score += scoreObjeto;
        }

        for (Objeto objeto : objetoService.listarObjetosPorElicitacao(elicitacao)) {
            float scoreObjeto = 0;

            //Chegada
            Set<Relacao> relacoesChegada = relacaoService.buscarRelacoesPorDestino(objeto);

            int qtdeR = getQtdeR(relacoesChegada);
            int qtdeB = getQtdeB(relacoesChegada);

            if (qtdeR > 0 || qtdeB > 0) {
                //Somente R
                if (qtdeB == 0 && qtdeR > 0) {
                    scoreObjeto += getValorObjeto2(objeto) + getSomaR(relacoesChegada);
                }
                //Somente B
                else if (qtdeR == 0 && qtdeB > 0) {
                    scoreObjeto += (getValorObjeto2(objeto) + getSomaB(relacoesChegada)) / (qtdeB + 1);
                }
                else if (getSomaR(relacoesChegada) > getSomaB(relacoesChegada)) {
                    scoreObjeto = getValorObjeto2(objeto) + getSomaR(relacoesChegada) - getSomaB(relacoesChegada);
                }
                else if (getSomaR(relacoesChegada) < getSomaB(relacoesChegada)) {
                    scoreObjeto = getSomaB(relacoesChegada);
                }
                else {
                    scoreObjeto = getValorObjeto2(objeto);
                }
            }

            score += scoreObjeto;
        }

        return score;
    }

    private static int getQtdeR(Set<Relacao> relacoes) {
        int qtde = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.R) {
                qtde++;
            }
        }

        return qtde;
    }

    private static int getQtdeB(Set<Relacao> relacoes) {
        int qtde = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.B) {
                qtde++;
            }
        }

        return qtde;
    }

    private static float getSomaR(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.R) {
                soma += getValorObjeto(relacao.getDestino());
            }
        }

        return soma;
    }

    private static float getSomaROrigem(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.R) {
                soma += getValorObjeto(relacao.getOrigem());
            }
        }

        return soma;
    }

    private static float getSomaB(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.B) {
                soma += getValorObjeto(relacao.getDestino());
            }
        }

        return soma;
    }

    private static float getSomaBOrigem(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.B) {
                soma += getValorObjeto(relacao.getOrigem());
            }
        }

        return soma;
    }

    private static float getMediaB(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.R) {
                soma += getValorObjeto(relacao.getDestino());
            }
        }

        if (soma > 0) {
            soma = soma / relacoes.size();
        }

        return soma;
    }

    private static float getMediaBOrigem(Set<Relacao> relacoes) {
        int soma = 0;

        for (Relacao relacao : relacoes) {
            if (relacao.getPeso() != null && relacao.getPeso() == RelacaoPeso.B) {
                soma += getValorObjeto(relacao.getOrigem());
            }
        }

        if (soma > 0) {
            soma = soma / relacoes.size();
        }

        return soma;
    }

    private static float getValorObjeto(Objeto objeto) {
        if (objeto.getControlabilidadeAgente() != null && objeto.getControlabilidadeDominio() != null) {
            switch (objeto.getControlabilidadeAgente()) {
                case CT:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 9;
                        case PN:
                            return 8;
                        case NC:
                            return 7;
                    }
                case PN:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 3;
                        case PN:
                            return 2;
                        case NC:
                            return 1;
                    }
                case NC:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 6;
                        case PN:
                            return 5;
                        case NC:
                            return 4;
                    }
            }
        }

        return 0;
    }

    private static float getValorObjeto2(Objeto objeto) {
        if (objeto.getControlabilidadeAgente() != null && objeto.getControlabilidadeDominio() != null) {
            switch (objeto.getControlabilidadeAgente()) {
                case CT:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 7;
                        case PN:
                            return 8;
                        case NC:
                            return 9;
                    }
                case PN:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 1;
                        case PN:
                            return 2;
                        case NC:
                            return 3;
                    }
                case NC:
                    switch (objeto.getControlabilidadeDominio()) {
                        case CT:
                            return 4;
                        case PN:
                            return 5;
                        case NC:
                            return 6;
                    }
            }
        }

        return 0;
    }
}
