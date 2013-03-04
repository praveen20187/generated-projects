/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/main/java/project/repository/support/RepositoryImpl.p.vm.java
 */
package com.jaxio.repository.support;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.transaction.annotation.Transactional;

import com.jaxio.dao.support.GenericDao;
import com.jaxio.dao.support.SearchParameters;
import com.jaxio.domain.Identifiable;

/**
 * Default implementation of the {@link Repository}
 * @see Repository
 */
public abstract class RepositoryImpl<T extends Identifiable<PK>, PK extends Serializable> implements Repository<T, PK> {

    private Logger log;

    public RepositoryImpl() {
        this.log = Logger.getLogger(getClass());
    }

    abstract public GenericDao<T, PK> getDao();

    /**
     * {@inheritDoc}
     */
    public abstract T getNew();

    /**
     * {@inheritDoc}
     */
    public abstract T getNewWithDefaults();

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void save(T model) {
        getDao().save(model);
    }

    @Transactional
    public T merge(T model) {
        return getDao().merge(model);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T getById(PK id) {
        T entity = getNew();
        entity.setId(id);
        return get(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void deleteById(PK id) {
        delete(getById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T get(T model) {
        return getDao().get(model);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void delete(T model) {
        if (model == null) {
            if (log.isDebugEnabled()) {
                log.debug("Skipping deletion for null model!");
            }

            return;
        }

        getDao().delete(model);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public void refresh(T entity) {
        getDao().refresh(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T findUnique(T model) {
        return findUnique(model, new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T findUnique(T model, SearchParameters sp) {
        return getDao().findUnique(model, sp);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T findUniqueOrNone(T model) {
        return findUniqueOrNone(model, new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T findUniqueOrNone(T model, SearchParameters sp) {
        return getDao().findUniqueOrNone(model, sp);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<T> find() {
        return find(getNew(), new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<T> find(T model) {
        return find(model, new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<T> find(SearchParameters sp) {
        return find(getNew(), sp);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<T> find(T model, SearchParameters sp) {
        return getDao().find(model, sp);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int findCount() {
        return findCount(getNew(), new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int findCount(T model) {
        return findCount(model, new SearchParameters());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int findCount(SearchParameters sp) {
        return findCount(getNew(), sp);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int findCount(T model, SearchParameters sp) {
        return getDao().findCount(model, sp);
    }
}