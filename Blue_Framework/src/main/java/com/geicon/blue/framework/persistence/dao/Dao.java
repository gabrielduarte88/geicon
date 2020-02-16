package com.geicon.blue.framework.persistence.dao;

import com.geicon.blue.framework.persistence.models.Entity;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * Objeto de acesso às entidades do sistema
 *
 * @author Gabriel Duarte
 * @param <T> Classe alvo
 */
public interface Dao<T extends Entity> {
    /**
     * Obtém a sessão
     *
     * @return Hibernate session
     */
    public Session getSession();

    /**
     * Altera a Hibernate session
     *
     * @param session Hibernate session
     */
    public void setSession(Session session);

    /**
     * Cria um Criteria a partir do DAO
     *
     * @return novo Criteria
     */
    public Criteria createCriteria();

    /**
     * Obtém o resultado único de uma consulta
     *
     * @param criteria Critérios da consulta Hibernate
     * @return Resultado único
     */
    public Object uniqueResult(Criteria criteria);

    /**
     * Obtém uma entidade de acordo com os critérios informadas
     *
     * @param criteria Critérios da consulta Hibernate
     * @return Entidade encontrada
     */
    public T load(Criteria criteria);

    /**
     * Retorna a lista de entidades correspondentes
     *
     * @return Lista de entidades correspondentes
     */
    public Set<T> list();

    /**
     * Retorna a lista de entidades correspondentes
     *
     * @param criteria Critérios da consulta Hibernate
     * @return Lista de entidades correspondentes
     */
    public Set<T> list(Criteria criteria);

    /**
     * Retorna a lista de entidades correspondentes
     *
     * @param criteria Critérios da consulta Hibernate
     * @param first Primeiro elemento
     * @param step Quantidade de elementos
     * @return Lista de entidades correspondentes
     */
    public Set<T> list(Criteria criteria, int first, int step);

    /**
     * Retorna a quantidade de entidades correspondentes
     *
     * @return Quantidade de entidades
     */
    public int count();

    /**
     * Retorna a quantidade de entidades correspondentes
     *
     * @param criteria Critérios da consulta Hibernate
     * @return Quantidade de entidades
     */
    public int count(Criteria criteria);

    /**
     * Insere uma nova entidade
     *
     * @param entity Nova entidade
     * @return ID da entidade inserida
     * @throws PersistenceException
     */
    public Integer insert(T entity) throws PersistenceException;

    /**
     * Atualiza uma entidade
     *
     * @param entity Entidade alvo
     * @throws PersistenceException
     */
    public void update(T entity) throws PersistenceException;

    /**
     * Remove uma entidade
     *
     * @param entity Entidade a ser removida
     * @throws PersistenceException
     */
    public void delete(T entity) throws PersistenceException;
}
