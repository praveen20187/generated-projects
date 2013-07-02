/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-backend-jpa:src/main/java/project/dao/support/PropertySelector.p.vm.java
 */
package com.jaxio.dao.support;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Used to construct OR predicate for a property value. In other words you can search
 * all entities E having a given property set to one of the selected values.
 */
public class PropertySelector<E, F> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final SingularAttribute<E, F> field;
    private List<F> selected = newArrayList();
    private SearchMode searchMode; // for string property only.

    /**
     * @param field the property that should match one of the selected value.
     */
    public PropertySelector(SingularAttribute<E, F> field) {
        this.field = checkNotNull(field);
    }

    public SingularAttribute<E, F> getField() {
        return field;
    }

    /**
     * Get the possible candidates for property.
     */
    public List<F> getSelected() {
        return selected;
    }

    /**
     * Set the possible candidates for property.
     */
    public void setSelected(List<F> selected) {
        if (selected == null) {
            clearSelected();
        } else {
            this.selected = selected;
        }
    }

    /**
     * Add a possible candidates for property.
     */
    public void add(F selected) {
        this.selected.add(selected);
    }

    public boolean isNotEmpty() {
        return selected != null && !selected.isEmpty();
    }

    public void clearSelected() {
        if (selected != null) {
            selected.clear();
        }
    }

    public boolean isBoolean() {
        return field.getJavaType().isAssignableFrom(Boolean.class);
    }

    public SearchMode getSearchMode() {
        return searchMode;
    }

    /**
     * In case, the field's type is a String, you can set a searchMode to use.
     * It is null by default.
     */
    public void setSearchMode(SearchMode searchMode) {
        this.searchMode = checkNotNull(searchMode);
    }

    /**
     * {@link PropertySelector} builder
     */
    public static <E, F> PropertySelector<E, F> newPropertySelector(SingularAttribute<E, F> field) {
        return new PropertySelector<E, F>(field);
    }

    /**
     * {@link PropertySelector} builder
     */
    public static <E, F> PropertySelector<E, F> newPropertySelector(SingularAttribute<E, F> field, SearchMode searchMode) {
        PropertySelector<E, F> ps = new PropertySelector<E, F>(field);
        ps.setSearchMode(searchMode);
        return ps;
    }
}