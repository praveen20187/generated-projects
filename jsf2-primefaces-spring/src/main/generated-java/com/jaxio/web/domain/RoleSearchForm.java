/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring-conversation:src/main/java/domain/SearchForm.e.vm.java
 */
package com.jaxio.web.domain;

import static com.jaxio.dao.support.PropertySelector.newPropertySelector;
import static com.jaxio.domain.Role_.roleName;
import javax.inject.Named;
import com.jaxio.dao.support.PropertySelector;
import com.jaxio.dao.support.SearchParameters;
import com.jaxio.domain.Role;
import com.jaxio.web.domain.support.GenericSearchForm;
import com.jaxio.web.faces.Conversation;

/**
 * View Helper to find/select {@link Role}.
 * It exposes a {@link Role} instance so it can be used in search by Example query.
 */
@Named
@Conversation
public class RoleSearchForm extends GenericSearchForm<Role, Integer, RoleSearchForm> {
    private static final long serialVersionUID = 1L;

    private Role role = new Role();
    private PropertySelector<Role, String> roleNameSelector = newPropertySelector(roleName);

    public Role getRole() {
        return role;
    }

    @Override
    protected Role getEntity() {
        return role;
    }

    // Selectors for property
    public PropertySelector<Role, String> getRoleNameSelector() {
        return roleNameSelector;
    }

    public SearchParameters toSearchParameters() {
        return new SearchParameters() //
                .anywhere() //
                .property(roleNameSelector) //
        ;
    }

    @Override
    public RoleSearchForm newInstance() {
        return new RoleSearchForm();
    }

    @Override
    public void resetWithOther(RoleSearchForm other) {
        this.role = other.getRole();
        this.roleNameSelector = other.getRoleNameSelector();
    }
}
