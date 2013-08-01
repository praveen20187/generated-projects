/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-backend-jpa:src/main/java/repository/support/TermSelector.p.vm.java
 */
package com.jaxio.repository.support;

import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

public class TermSelector implements Serializable {
    private static final long serialVersionUID = 1L;
    private final SingularAttribute<?, ?> attribute;
    private List<String> selected = newArrayList();

    public TermSelector(SingularAttribute<?, ?> attribute) {
        this.attribute = attribute;
    }

    public TermSelector() {
        this(null);
    }

    public SingularAttribute<?, ?> getAttribute() {
        return attribute;
    }

    /**
     * Get the possible candidates for property.
     */
    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = newArrayList(selected);
    }

    /**
     * Set the possible candidates for property.
     */
    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public boolean isNotEmpty() {
        return (selected != null) && !selected.isEmpty();
    }

    public void clearSelected() {
        if (selected != null) {
            selected.clear();
        }
    }
}