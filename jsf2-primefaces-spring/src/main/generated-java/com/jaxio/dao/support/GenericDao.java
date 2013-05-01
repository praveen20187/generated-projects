/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/main/java/project/dao/support/GenericDao.p.vm.java
 */
package com.jaxio.dao.support;

import static com.jaxio.dao.support.JpaUtil.buildJpaOrders;
import static com.jaxio.dao.support.ByEntitySelectorUtil.byEntitySelectors;
import static com.jaxio.dao.support.ByPropertySelectorUtil.byPropertySelectors;
import static com.jaxio.dao.support.ByRangeUtil.byRanges;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.jaxio.domain.Identifiable;

/**
 * JPA 2 Generic DAO with find by example/range/pattern and CRUD support. 
 */
public abstract class GenericDao<E extends Identifiable<PK>, PK extends Serializable> {

    @Inject
    private ByExampleUtil byExampleUtil;
    @Inject
    private ByPatternUtil byPatternUtil;
    @Inject
    private NamedQueryUtil namedQueryUtil;
    @PersistenceContext
    private EntityManager entityManager;

    private Class<E> type;
    private Logger log;
    private String cacheRegion;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected ByExampleUtil getByExampleUtil() {
        return byExampleUtil;
    }

    protected ByPatternUtil getByPatternUtil() {
        return byPatternUtil;
    }

    protected NamedQueryUtil getNamedQueryUtil() {
        return namedQueryUtil;
    }

    /**
     * This constructor needs the real type of the generic type E so it can be passed to the {@link EntityManager}.
     */
    public GenericDao(Class<E> type) {
        this.type = type;
        this.log = Logger.getLogger(getClass());
        this.cacheRegion = type.getCanonicalName();
    }

    /**
     * Gets from the repository the E entity instance.
     * 
     * DAO for the local database will typically use the primary key or unique fields of the passed entity, while DAO for external repository may use a unique
     * field present in the entity as they probably have no knowledge of the primary key. Hence, passing the entity as an argument instead of the primary key
     * allows you to switch the DAO more easily.
     * 
     * @param entity an E instance having a primary key set
     * @return the corresponding E persistent instance or null if none could be found.
     */
    public E get(E entity) {
        if (entity == null) {
            return null;
        }

        Serializable id = entity.getId();
        if (id == null) {
            return null;
        }

        E entityFound = getEntityManager().find(type, id);

        if (entityFound == null) {
            log.warn("get returned null with pk=" + id);
        }

        return entityFound;
    }

    /**
     * Refresh the passed entity with up to date data. Does nothing if the passed entity is a new entity (not yet managed).
     * 
     * @param entity the entity to refresh.
     */
    public void refresh(E entity) {
        if (entityManager.contains(entity)) {
            entityManager.refresh(entity);
        }
    }

    /**
     * Find and load a list of E instance.
     * 
     * @param entity a sample entity whose non-null properties may be used as search hints
     * @param searchParameters carries additional search information
     * @return the entities matching the search.
     */
    @SuppressWarnings("unchecked")
    public List<E> find(E entity, SearchParameters sp) {
        if (sp.hasNamedQuery()) {
            return getNamedQueryUtil().findByNamedQuery(sp);
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(type);
        if (sp.getDistinct()) {
            criteriaQuery.distinct(true);
        }
        Root<E> root = criteriaQuery.from(type);

        // predicate
        Predicate predicate = getPredicate(root, criteriaQuery, builder, entity, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        // left join
        if (sp.hasLeftJoins()) {
            for (SingularAttribute<?, ?> arg : sp.getLeftJoins()) {
                root.fetch((SingularAttribute<E, ?>) arg, JoinType.LEFT);
            }
        }

        // order by
        criteriaQuery.orderBy(buildJpaOrders(sp.getOrders(), root, builder, sp));

        TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);

        // cache
        setCacheHints(typedQuery, sp);

        // pagination
        if (sp.getFirstResult() >= 0) {
            typedQuery.setFirstResult(sp.getFirstResult());
        }
        if (sp.getMaxResults() > 0) {
            typedQuery.setMaxResults(sp.getMaxResults());
        }

        // execution
        List<E> entities = typedQuery.getResultList();
        if (log.isDebugEnabled()) {
            log.debug("Returned " + entities.size() + " elements");
        }

        return entities;
    }

    /**
     * Count the number of E instances.
     * 
     * @param entity a sample entity whose non-null properties may be used as search hint
     * @param searchParameters carries additional search information
     * @return the number of entities matching the search.
     */
    public int findCount(E entity, SearchParameters sp) {
        Validate.notNull(entity, "The entity cannot be null");

        if (sp.hasNamedQuery()) {
            return getNamedQueryUtil().numberByNamedQuery(sp).intValue();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);

        if (sp.getDistinct()) {
            criteriaQuery = criteriaQuery.select(builder.countDistinct(root));
        } else {
            criteriaQuery = criteriaQuery.select(builder.count(root));
        }

        // predicate
        Predicate predicate = getPredicate(root, criteriaQuery, builder, entity, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

        // cache
        setCacheHints(typedQuery, sp);

        // execution
        Long count = typedQuery.getSingleResult();

        if (count != null) {
            return count.intValue();
        } else {
            log.warn("findCount returned null!");
            return 0;
        }
    }

    public E findUnique(E entity, SearchParameters sp) {
        E result = findUniqueOrNone(entity, sp);

        if (result == null) {
            throw new NoResultException("Developper: You expected 1 result but we found none ! sample: " + entity);
        }

        return result;
    }

    /**
     * We request at most 2, if there's more than one then we throw a  {@link NonUniqueResultException}
     * @throws NonUniqueResultException
     */
    public E findUniqueOrNone(E entity, SearchParameters sp) {
        // this code is an optimization to prevent using a count
        sp.setFirstResult(0);
        sp.setMaxResults(2);
        List<E> results = find(entity, sp);

        if (results == null || results.isEmpty()) {
            return null;
        }

        if (results.size() > 1) {
            throw new NonUniqueResultException("Developper: You expected 1 result but we found more ! sample: " + entity);
        }

        return results.iterator().next();
    }

    protected <R> Predicate getPredicate(Root<E> root, CriteriaQuery<R> query, CriteriaBuilder builder, E entity, SearchParameters sp) {
        return JpaUtil.andPredicate(builder, //
                byRanges(root, query, builder, sp.getRanges(), type), //
                byPropertySelectors(root, builder, sp, sp.getProperties()), //
                byEntitySelectors(root, builder, sp.getEntities()), //
                getByExamplePredicate(root, entity, sp, builder), //
                byPatternUtil.byPattern(root, query, builder, sp, type), //
                getExtraPredicate(root, query, builder, entity, sp));
    }

    protected Predicate getByExamplePredicate(Root<E> root, E entity, SearchParameters sp, CriteriaBuilder builder) {
        return byExampleUtil.byExampleOnEntity(root, entity, sp, builder);
    }

    /**
     * You may override this method to add a Predicate to the default find method.
     */
    protected <R> Predicate getExtraPredicate(Root<E> root, CriteriaQuery<R> query, CriteriaBuilder builder, E entity, SearchParameters sp) {
        return null;
    }

    /**
     * Save or update the passed entity E to the repository.
     * Assume that the entity is already present in the persistence context.
     * No merge is done.
     * 
     * @param entity the entity to be saved or updated.
     */
    public void save(E entity) {
        Validate.notNull(entity, "The entity to save cannot be null element");

        // creation with auto generated id
        if (!entity.isIdSet()) {
            getEntityManager().persist(entity);
            return;
        }

        // creation with manually assigned key
        if (JpaUtil.isEntityIdManuallyAssigned(type) && !getEntityManager().contains(entity)) {
            getEntityManager().persist(entity);
            return;
        }
        // other cases are update
        // the simple fact to invoke this method, from a service method annotated with @Transactional,
        // does the job (assuming the passed entity is present in the persistence context)
    }

    /**
     * Persist the passed entity.
     */
    public void persist(E entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Merge the state of the given entity into the current persistence context.
     */
    public E merge(E entity) {
        return getEntityManager().merge(entity);
    }

    /**
     * Delete the passed entity E from the repository.
     * 
     * @param entity the entity to be deleted.
     */
    public void delete(E entity) {
        if (getEntityManager().contains(entity)) {
            getEntityManager().remove(entity);
        } else {
            // could be a delete on a transient instance
            E entityRef = getEntityManager().getReference(type, entity.getId());

            if (entityRef != null) {
                getEntityManager().remove(entityRef);
            } else {
                log.warn("Attempt to delete an instance that is not present in the database: " + entity.toString());
            }
        }
    }

    // -----------------
    // Util
    // -----------------

    /**
     * Return the optimistic version value, if any.
     */
    @SuppressWarnings("unchecked")
    public Comparable<Object> getVersion(E entity) {
        EntityType<E> entityType = entityManager.getMetamodel().entity(type);
        if (!entityType.hasVersionAttribute()) {
            return null;
        }
        // _HACK_ too bad that JPA does not provide this entityType.getVersion();
        // read: http://stackoverflow.com/questions/13265094/generic-way-to-get-jpa-entity-version

        SingularAttribute<? super E, ?> version = null;
        for (SingularAttribute<? super E, ?> sa : entityType.getSingularAttributes()) {
            if (sa.isVersion()) {
                version = sa;
                break;
            }
        }

        return (Comparable<Object>) JpaUtil.getValue(entity, version);
    }

    // -----------------
    // Commons
    // -----------------

    /**
     * Set hints for 2d level cache.
     */
    protected void setCacheHints(TypedQuery<?> typedQuery, SearchParameters sp) {
        if (sp.isCacheable()) {
            typedQuery.setHint("org.hibernate.cacheable", true);

            if (sp.hasCacheRegion()) {
                typedQuery.setHint("org.hibernate.cacheRegion", sp.getCacheRegion());
            } else {
                typedQuery.setHint("org.hibernate.cacheRegion", cacheRegion);
            }
        }
    }

    // -----------------
    // Hibernate Search
    // -----------------
    protected String[] getIndexedFields() {
        return new String[0];
    }
}