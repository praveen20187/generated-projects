/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-jsf2-spring-conversation:src/main/java/domain/Controller.e.vm.java
 */
package com.jaxio.web.domain;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.jaxio.domain.Role;
import com.jaxio.domain.Role_;
import com.jaxio.printer.RolePrinter;
import com.jaxio.repository.RoleRepository;
import com.jaxio.repository.support.SearchParameters;
import com.jaxio.web.domain.support.GenericController;
import com.jaxio.web.permission.RolePermission;

/**
 * Stateless controller for {@link Role} conversation start.
 */
@Named
@Singleton
public class RoleController extends GenericController<Role, Integer> {
    public static final String ROLE_EDIT_URI = "/domain/roleEdit.faces";
    public static final String ROLE_SELECT_URI = "/domain/roleSelect.faces";

    @Inject
    public RoleController(RoleRepository roleRepository, RolePermission rolePermission, RolePrinter rolePrinter) {
        super(roleRepository, rolePermission, rolePrinter, ROLE_SELECT_URI, ROLE_EDIT_URI);
    }

    @Override
    protected SearchParameters defaultOrder(SearchParameters searchParameters) {
        return searchParameters.orderBy(Role_.roleName);
    }
}