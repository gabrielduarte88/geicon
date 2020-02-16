package com.geicon.blue.framework.persistence.services;

import com.geicon.blue.framework.persistence.dao.Dao;
import com.geicon.blue.framework.persistence.models.Entity;
import java.text.ParseException;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;

/**
 * Serviços relativos as entidades
 *
 * @author Gabriel Duarte
 * @param <T> Classe alvo
 */
public interface Service<T extends Entity> {
    /**
     * Retorna o DAO
     *
     * @return DAO
     */
    public Dao<T> getDao();

    /**
     * Cria um Criteria a partir do DAO
     *
     * @return novo Criteria
     */
    public Criteria createCriteria();

    /**
     * Adiciona um filtro em um Criteria
     *
     * @param criteria Criteria adicionado
     * @param filterType Tipo do campo de filtro
     * @param filterField Campo do filtro
     * @param filterValue Valor do campo de filtro
     * @return Criteria com o filtro
     * @throws java.text.ParseException
     */
    public Criteria parseFilter(Criteria criteria, String filterType, String filterField, String filterValue) throws ParseException;

    /**
     * Obtém uma entidade com os critérios informados através do DAO
     *
     * @param criteria Critérios Hibernate
     * @return Entidade encontrada
     */
    public T load(Criteria criteria);

    /**
     * Retorna a lista de entidades correspondentes através do DAO
     *
     * @param criteria Critérios Hibernate
     * @return Lista de entidades correspondentes
     */
    public Set<T> list(Criteria criteria);

    /**
     * Retorna a lista de entidades correspondentes através do DAO
     *
     * @param criteria Critérios Hibernate
     * @param first Primeiro elemento
     * @param step Quantidade de elementos
     * @return Lista de entidades correspondentes
     */
    public Set<T> list(Criteria criteria, int first, int step);

    /**
     * Retorna a quantidade de entidades correspondentes através do DAO
     *
     * @param criteria Critérios Hibernate
     * @return Quantidade de entidades
     */
    public int count(Criteria criteria);

    /**
     * Insere uma nova entidade através do DAO
     *
     * @param entity Nova entidade
     * @return ID da entidade inserida
     * @throws PersistenceException
     */
    public Integer insert(T entity) throws PersistenceException;

    /**
     * Atualiza uma entidade através do DAO
     *
     * @param entity Entidade alvo
     * @throws PersistenceException
     */
    public void update(T entity) throws PersistenceException;

    /**
     * Remove uma entidade através do DAO
     *
     * @param entity Entidade a ser removida
     * @throws PersistenceException
     */
    public void delete(T entity) throws PersistenceException;
}
