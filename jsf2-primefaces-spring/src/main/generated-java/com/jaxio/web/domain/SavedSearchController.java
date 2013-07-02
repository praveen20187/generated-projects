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

import com.jaxio.domain.SavedSearch;
import com.jaxio.printer.SavedSearchPrinter;
import com.jaxio.repository.SavedSearchRepository;
import com.jaxio.web.domain.support.GenericController;
import com.jaxio.web.permission.SavedSearchPermission;

/**
 * Stateless controller for {@link SavedSearch} conversation start. 
 */
@Named
@Singleton
public class SavedSearchController extends GenericController<SavedSearch, Integer> {
    public static final String SAVEDSEARCH_EDIT_URI = "/domain/savedSearchEdit.faces";
    public static final String SAVEDSEARCH_SELECT_URI = "/domain/savedSearchSelect.faces";

    @Inject
    public SavedSearchController(SavedSearchRepository savedSearchRepository, SavedSearchPermission savedSearchPermission, SavedSearchPrinter savedSearchPrinter) {
        super(savedSearchRepository, savedSearchPermission, savedSearchPrinter, SAVEDSEARCH_SELECT_URI, SAVEDSEARCH_EDIT_URI);
    }

    public SavedSearchFileUpload getSavedSearchFileUpload(SavedSearch savedSearch) {
        return new SavedSearchFileUpload(savedSearch);
    }
}