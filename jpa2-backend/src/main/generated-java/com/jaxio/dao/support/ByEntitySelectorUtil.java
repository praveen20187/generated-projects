/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-backend-jpa:src/main/java/project/dao/support/ByEntitySelectorUtil.p.vm.java
 */
package com.jaxio.dao.support;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.TRUE;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.jaxio.domain.Identifiable;

/**
 * Helper to create a predicate out of {@link EntitySelector}s.
 */
public class ByEntitySelectorUtil {

    public static <E> Predicate byEntitySelectors(Root<E> root, CriteriaBuilder builder, SearchParameters sp) {
        List<EntitySelector<?, ?, ?>> selectors = sp.getEntities();
        List<Predicate> predicates = newArrayList();

        for (EntitySelector<?, ?, ?> s : selectors) {
            @SuppressWarnings("unchecked")
            EntitySelector<E, ? extends Identifiable<?>, ?> selector = (EntitySelector<E, ? extends Identifiable<?>, ?>) s;

            if (selector.isNotEmpty()) {
                List<Predicate> selectorPredicates = newArrayList();

                for (Identifiable<?> selection : selector.getSelected()) {
                    selectorPredicates.add(builder.equal(getExpression(root, selector), selection.getId()));
                }

                if (TRUE == selector.getIncludeNull()) {
                    selectorPredicates.add(builder.or(builder.isNull(getExpression(root, selector))));
                }

                predicates.add(JpaUtil.orPredicate(builder, selectorPredicates));
            } else if (selector.isIncludeNullSet()) {
                if (selector.getIncludeNull()) {
                    predicates.add(builder.isNull(getExpression(root, selector)));
                } else {
                    predicates.add(builder.isNotNull(getExpression(root, selector)));
                }
            }
        }

        return JpaUtil.concatPredicate(sp, builder, predicates);
    }

    private static <E> Expression<?> getExpression(Root<E> root, EntitySelector<E, ? extends Identifiable<?>, ?> selector) {
        if (selector.getField() != null) {
            return root.get(selector.getField()).get("id");
        } else {
            return root.get(selector.getCpkField()).get(selector.getCpkInnerField().getName());
        }
    }
}