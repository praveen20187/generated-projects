/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-jsf2-spring-conversation:src/main/java/domain/support/GenericController.p.vm.java
 */
package com.jaxio.web.domain.support;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.newArrayList;
import static com.jaxio.repository.support.MetamodelUtil.toAttribute;
import static com.jaxio.web.conversation.ConversationHolder.getCurrentConversation;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.jaxio.domain.Identifiable;
import com.jaxio.printer.support.GenericPrinter;
import com.jaxio.printer.support.TypeAwarePrinter;
import com.jaxio.repository.support.GenericRepository;
import com.jaxio.repository.support.JpaUniqueUtil;
import com.jaxio.repository.support.SearchParameters;
import com.jaxio.repository.support.TermSelector;
import com.jaxio.web.conversation.ConversationCallBack;
import com.jaxio.web.conversation.ConversationContext;
import com.jaxio.web.conversation.ConversationManager;
import com.jaxio.web.permission.support.GenericPermission;
import com.jaxio.web.util.MessageUtil;

/**
 * Base controller for JPA entities providing helper methods to:
 * <ul>
 *  <li>start conversations</li>
 *  <li>create conversation context</li>
 *  <li>support autoComplete component</li>
 *  <li>perform actions</li>
 *  <li>support excel export</li>
 * </ul>
 */
public abstract class GenericController<E extends Identifiable<PK>, PK extends Serializable> {
    private static final Logger log = LoggerFactory.getLogger(GenericController.class);
    private static final String PERMISSION_DENIED = "/error/accessdenied";
    private String selectUri;
    private String editUri;

    @Inject
    protected ConversationManager conversationManager;
    @Inject
    protected JpaUniqueUtil jpaUniqueUtil;
    @Inject
    protected MessageUtil messageUtil;
    @Inject
    protected TypeAwarePrinter typeAwarePrinter;
    protected GenericRepository<E, PK> repository;
    protected GenericPermission<E> permission;
    protected GenericPrinter<E> printer;

    public GenericController(GenericRepository<E, PK> repository, GenericPermission<E> permission, GenericPrinter<E> printer, String selectUri, String editUri) {
        this.repository = checkNotNull(repository);
        this.permission = checkNotNull(permission);
        this.printer = checkNotNull(printer);
        this.selectUri = checkNotNull(selectUri);
        this.editUri = checkNotNull(editUri);
    }

    public final GenericRepository<E, PK> getRepository() {
        return repository;
    }

    public final GenericPermission<E> getPermission() {
        return permission;
    }

    public final MessageUtil getMessageUtil() {
        return messageUtil;
    }

    public String getPermissionDenied() {
        return PERMISSION_DENIED;
    }

    public String getSelectUri() {
        return selectUri;
    }

    public String getEditUri() {
        return editUri;
    }

    // ----------------------------------------
    // START CONVERSATION PROGRAMATICALY
    // ----------------------------------------

    /**
     * Start a new {@link Conversation} that allows the user to search an existing entity.
     * This method can be invoked from an h:commandLink's action attribute.
     * @return the implicit first view for the newly created conversation.
     */
    public String beginSearch() {
        if (!permission.canSearch()) {
            return getPermissionDenied();
        }
        return beginConversation(newSearchContext(getSearchLabelKey()));
    }

    /**
     * Start a new {@link Conversation} that allows the user to create a new entity.
     * This method can be invoked from an h:commandLink's action attribute.
     * @return the implicit first view for the newly created conversation.
     */
    public String beginCreate() {
        if (!permission.canCreate()) {
            return getPermissionDenied();
        }
        return beginConversation(newEditContext(getCreateLabelKey(), repository.getNewWithDefaults()));
    }

    /**
     * Start a new {@link Conversation} using the passed ctx as the first conversation context.
     * @return the implicit first view for the newly created conversation.
     */
    public String beginConversation(ConversationContext<E> ctx) {
        return conversationManager.beginConversation(ctx).nextView();
    }

    // ----------------------------------------
    // AUTO COMPLETE SUPPORT  
    // ----------------------------------------

    /**
     * Auto-complete support. This method is used by primefaces autoComplete component.
     */
    public List<E> complete(String value) {
        try {
            SearchParameters searchParameters = new SearchParameters().limitBroadSearch().distinct().orMode();
            E template = repository.getNew();
            for (String property : completeProperties()) {
                if (repository.isIndexed(property)) {
                    TermSelector termSelector = new TermSelector(toAttribute(property, repository.getType()));
                    termSelector.setSelected(value);
                    searchParameters.addTerm(termSelector);
                } else {
                    PropertyUtils.setProperty(template, property, value);
                }
            }
            return repository.find(template, searchParameters);
        } catch (Exception e) {
            log.warn("error during complete", e);
            throw propagate(e);
        }
    }

    private Iterable<String> completeProperties() {
        String completeOnProperties = parameter("completeOnProperties", String.class);
        return isBlank(completeOnProperties) ? printer.getDisplayedAttributes() : Splitter.on(";,").omitEmptyStrings().split(completeOnProperties);
    }

    public List<String> completeProperty(String value) {
        String property = parameter("property", String.class);
        Integer maxResults = parameter("maxResults", Integer.class);
        return completeProperty(value, property, maxResults);
    }

    public List<String> completeProperty(String value, String property) {
        return completeProperty(value, property, null);
    }

    public List<String> completeProperty(String toMatch, String property, Integer maxResults) {
        List<String> values = newArrayList();
        if (repository.isIndexed(property)) {
            values.addAll(completePropertyUsingFullText(toMatch, property, maxResults));
        } else {
            values.addAll(completePropertyInDatabase(toMatch, property, maxResults));
        }
        if (isBlank(toMatch) || values.contains(toMatch)) {
            // the term is already in the results, return them directly
            return values;
        } else {
            // add the term before the results as it is not part of the results
            List<String> retWithValue = newArrayList(toMatch);
            retWithValue.addAll(values);
            return retWithValue;
        }
    }

    private List<String> completePropertyUsingFullText(String term, String property, Integer maxResults) {
        try {
            SearchParameters searchParameters = new SearchParameters().limitBroadSearch().distinct();
            TermSelector termSelector = new TermSelector(toAttribute(property, repository.getType()));
            termSelector.setSelected(term);
            searchParameters.addTerm(termSelector);
            if (maxResults != null) {
                searchParameters.maxResults(maxResults);
            }
            return repository.findProperty(String.class, repository.getNew(), searchParameters, property);
        } catch (Exception e) {
            log.warn("error during completePropertyUsingFullText", e);
            throw propagate(e);
        }
    }

    private List<String> completePropertyInDatabase(String value, String property, Integer maxResults) {
        try {
            SearchParameters searchParameters = new SearchParameters().limitBroadSearch().caseInsensitive().anywhere().distinct();
            if (maxResults != null) {
                searchParameters.maxResults(maxResults);
            }
            E template = repository.getNew();
            PropertyUtils.setProperty(template, property, value);
            return repository.findProperty(String.class, template, searchParameters, property);
        } catch (Exception e) {
            log.warn("error during completePropertyInDatabase", e);
            throw propagate(e);
        }
    }

    /**
     * A simple autoComplete that returns exactly the input. It is used in search forms with {@link PropertySelector}.
     */
    public List<String> completeSame(String value) {
        return newArrayList(value);
    }

    @SuppressWarnings("unchecked")
    private <T> T parameter(String propertyName, Class<T> expectedType) {
        return (T) UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get(propertyName);
    }

    protected SearchParameters completeSearchParameters(String value) {
        return searchParameters().searchPattern(value);
    }

    protected SearchParameters searchParameters() {
        return defaultOrder(new SearchParameters() //
                .limitBroadSearch() //
                .distinct() //
                .anywhere() //
                .caseInsensitive());
    }

    protected SearchParameters defaultOrder(SearchParameters searchParameters) {
        return searchParameters;
    }

    /**
     * Decision helper used when handling ajax autoComplete event and regular page postback.
     */
    public boolean shouldReplace(E currentEntity, E selectedEntity) {
        if (currentEntity == selectedEntity) {
            return false;
        }

        if (currentEntity != null && selectedEntity != null && currentEntity.isIdSet() && selectedEntity.isIdSet()) {
            if (selectedEntity.getId().equals(currentEntity.getId())) {
                Comparable<Object> currentVersion = repository.getVersion(currentEntity);
                if (currentVersion == null) {
                    // assume no version at all is available
                    // let's stick with current entity.
                    return false;
                }
                Comparable<Object> selectedVersion = repository.getVersion(selectedEntity);
                if (currentVersion.compareTo(selectedVersion) == 0) {
                    // currentEntity could have been edited and not yet saved, we keep it.
                    return false;
                } else {
                    // we could have an optimistic locking exception at save time
                    // TODO: what should we do here?
                    return false;
                }
            }
        }
        return true;
    }

    // ----------------------------------------
    // CREATE IMPLICIT EDIT VIEW
    // ----------------------------------------

    /**
     * Helper to create a new {@link ConversationContext} to view the passed entity and set it as the current conversation's next context.  
     * The vars <code>sub</code> <code>readonly</code> are set to true.
     * The permission {@link GenericPermission#canView()} is checked.
     * 
     * @param labelKey label key for breadCrumb and conversation menu.
     * @param e the entity to view.
     * @return the implicit view to access this context.
     */
    public String editSubReadOnlyView(String labelKey, E e) {
        if (!permission.canView(e)) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = newEditContext(labelKey, e).sub().readonly();
        return getCurrentConversation().nextContext(ctx).view();
    }

    /**
     * Helper to create a new {@link ConversationContext} to edit the passed entity and set it as the current conversation's next context.  
     * The var <code>sub</code> is set to true.
     * The permission {@link GenericPermission#canEdit()} is checked.
     * 
     * @param labelKey label key for breadCrumb and conversation menu.
     * @param e the entity to edit.
     * @return the implicit view to access this context.
     */
    public String editSubView(String labelKey, E e, ConversationCallBack<E> editCallBack) {
        if (!permission.canEdit(e)) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = newEditContext(labelKey, e, editCallBack).sub();
        return getCurrentConversation().nextContext(ctx).view();
    }

    /**
     * Helper to create a new {@link ConversationContext} to create a new entity and set it as the current conversation's next context.  
     * The var <code>sub</code> is set to true.
     * 
     * @param labelKey label key for breadCrumb and conversation menu.
     * @return the implicit view to access this context.
     */
    public String createSubView(String labelKey, ConversationCallBack<E> createCallBack) {
        return createSubView(labelKey, repository.getNewWithDefaults(), createCallBack);
    }

    /**
     * Helper to create a new {@link ConversationContext} to edit the passed new entity and set it as the current conversation's next context.  
     * The var <code>sub</code> is set to true.
     * The permission {@link GenericPermission#canCreate()} is checked.
     * 
     * @param labelKey label key for breadCrumb and conversation menu.
     * @param e the entity to edit.
     * @return the implicit view to access this context.
     */
    public String createSubView(String labelKey, E e, ConversationCallBack<E> createCallBack) {
        if (!permission.canCreate()) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = newEditContext(labelKey, e, createCallBack).sub();
        return getCurrentConversation().nextContext(ctx).view();
    }

    // ----------------------------------------
    // CREATE IMPLICIT SELECT VIEW
    // ----------------------------------------

    public String selectSubView(String labelKey, ConversationCallBack<E> selectCallBack) {
        if (!permission.canSelect()) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = newSearchContext(labelKey, selectCallBack).sub();
        return getCurrentConversation().nextContext(ctx).view();
    }

    public String multiSelectSubView(String labelKey, ConversationCallBack<E> selectCallBack) {
        if (!permission.canSelect()) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = newSearchContext(labelKey, selectCallBack).sub();
        ctx.setVar("multiCheckboxSelection", true);
        return getCurrentConversation().nextContext(ctx).view();
    }

    // ----------------------------------------
    // CREATE EDIT CONVERSATION CONTEXT
    // ----------------------------------------

    /**
     * Helper to construct a new {@link ConversationContext} to edit an entity.
     * @param e the entity to edit.
     */
    public ConversationContext<E> newEditContext(E e) {
        return new ConversationContext<E>().entity(e).isNewEntity(!e.isIdSet()).viewUri(getEditUri());
    }

    public ConversationContext<E> newEditContext(String labelKey, E e) {
        return newEditContext(e).labelKey(labelKey);
    }

    public ConversationContext<E> newEditContext(String labelKey, E e, ConversationCallBack<E> callBack) {
        return newEditContext(labelKey, e).callBack(callBack);
    }

    // ----------------------------------------
    // CREATE SEARCH CONVERSATION CONTEXT
    // ----------------------------------------

    /**
     * Helper to construct a new {@link ConversationContext} for search/selection.
     */
    public ConversationContext<E> newSearchContext() {
        return new ConversationContext<E>(getSelectUri());
    }

    public ConversationContext<E> newSearchContext(String labelKey) {
        return newSearchContext().labelKey(labelKey);
    }

    public ConversationContext<E> newSearchContext(String labelKey, ConversationCallBack<E> callBack) {
        return newSearchContext(labelKey).callBack(callBack);
    }

    // ----------------------------------------
    // ACTIONS INVOKED FORM THE VIEW
    // ----------------------------------------

    public ConversationContext<E> getSelectedContext(E selected) {
        return newEditContext(getEditUri(), selected);
    }

    /**
     * Action to create a new entity.
     */
    public String create() {
        if (!permission.canCreate()) {
            return getPermissionDenied();
        }
        E newEntity = repository.getNewWithDefaults();
        ConversationContext<E> ctx = getSelectedContext(newEntity).labelKey(getCreateLabelKey());
        return getCurrentConversation().nextContext(ctx).view();
    }

    /**
     * Support for {@link GenericLazyDataModel.#edit()} and {@link GenericLazyDataModel#onRowSelect(org.primefaces.event.SelectEvent)} methods 
     */
    public String edit(E entity) {
        if (!permission.canEdit(entity)) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = getSelectedContext(entity).labelKey(getEditLabelKey(), typeAwarePrinter.print(entity));
        return getCurrentConversation().nextContext(ctx).view();
    }

    /**
     * Support for the {@link GenericLazyDataModel.#view()} method
     */
    public String view(E entity) {
        if (!permission.canView(entity)) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = getSelectedContext(entity).sub().readonly().labelKey(getViewLabelKey(), typeAwarePrinter.print(entity));
        return getCurrentConversation().nextContext(ctx).view();
    }

    /**
     * Return the print friendly view for the passed entity. I can be invoked directly from the view.
     */
    public String print(E entity) {
        if (!permission.canView(entity)) {
            return getPermissionDenied();
        }
        ConversationContext<E> ctx = getSelectedContext(entity).readonly().print().labelKey(getViewLabelKey(), typeAwarePrinter.print(entity));
        return getCurrentConversation().nextContext(ctx).view();
    }

    protected String select(E entity) {
        if (!permission.canSelect()) {
            return getPermissionDenied();
        }
        return getCurrentConversation() //
                .<ConversationContext<E>> getCurrentContext() //
                .getCallBack() //
                .selected(entity);
    }

    protected String getSearchLabelKey() {
        return getLabelName() + "_search";
    }

    protected String getCreateLabelKey() {
        return getLabelName() + "_create";
    }

    protected String getEditLabelKey() {
        return getLabelName() + "_edit";
    }

    protected String getViewLabelKey() {
        return getLabelName() + "_view";
    }

    protected String getLabelName() {
        return WordUtils.uncapitalize(getEntityName());
    }

    private String getEntityName() {
        return repository.getType().getSimpleName();
    }
}
