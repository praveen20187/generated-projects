/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-jsf2-spring-conversation:src/main/java/domain/support/SavedSearchService.p.vm.java
 */
package com.jaxio.web.domain.support;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import com.jaxio.context.UserContext;
import com.jaxio.domain.SavedSearch;
import com.jaxio.domain.SavedSearch_;
import com.jaxio.domain.User;
import com.jaxio.repository.SavedSearchRepository;
import com.jaxio.repository.UserRepository;
import com.jaxio.repository.support.PropertySelector;
import com.jaxio.repository.support.SearchParameters;
import com.jaxio.web.domain.support.GenericSearchForm;
import com.jaxio.web.util.MessageUtil;

/**
 * Store/Load Search Form Content to db.
 */
@Named
@Singleton
@SuppressWarnings({ "rawtypes" })
public class SavedSearchService {
    @Inject
    private SavedSearchRepository savedSearchRepository;
    @Inject
    private MessageUtil messageUtil;
    @Inject
    private UserRepository userRepository;

    @Transactional
    public void save(GenericSearchForm searchForm) {
        SavedSearch savedSearch = savedSearchRepository.findUniqueOrNone(example(searchForm));

        if (savedSearch == null) {
            savedSearch = example(searchForm);
        }

        if (searchForm.isPrivateSearch()) {
            savedSearch = markSearchPrivate(savedSearch);
        } else {
            savedSearch = markSearchPublic(savedSearch);
        }

        savedSearch.setFormContent(searchForm.toByteArray());
        savedSearchRepository.save(savedSearch);
        messageUtil.info("saved_search_saved", savedSearch.getName());

        // manual search form reset : reset only form metadata 
        searchForm.setSearchFormName(null);
        searchForm.setPrivateSearch(false);
    }

    private SavedSearch example(GenericSearchForm searchForm) {
        SavedSearch savedSearch = new SavedSearch();
        savedSearch.setName(searchForm.getSearchFormName());
        savedSearch.setFormClassname(searchForm.getClass().getSimpleName());
        return savedSearch;
    }

    /**
     * @param savedSearch
     * @return savedSearch marked as private with current user
     */
    private SavedSearch markSearchPrivate(SavedSearch savedSearch) {
        User currentUser = userRepository.getById(UserContext.getId());
        savedSearch.setUser(currentUser);
        return savedSearch;
    }

    private SavedSearch markSearchPublic(SavedSearch savedSearch) {
        return savedSearch.user(null);
    }

    private SavedSearch privateExample(GenericSearchForm searchForm) {
        return markSearchPrivate(example(searchForm));
    }

    public <F extends GenericSearchForm> Finder finderFor(F searchFrom) {
        // we use a Finder in order to have the required List<String> find(String) method
        return new Finder(savedSearchRepository, searchFrom);
    }

    public class Finder {
        private SavedSearchRepository savedSearchRepository;
        private GenericSearchForm searchFrom;

        public Finder(SavedSearchRepository savedSearchRepository, GenericSearchForm searchFrom) {
            this.savedSearchRepository = savedSearchRepository;
            this.searchFrom = searchFrom;
        }

        public List<SavedSearch> find(String name) {
            List<SavedSearch> searches = newArrayList();
            searches.addAll(findPrivate(name));
            searches.addAll(findPublic(name));
            return searches;
        }

        private List<SavedSearch> findPublic(String name) {
            List<SavedSearch> results = newArrayList();
            PropertySelector<SavedSearch, User> selector = PropertySelector.newPropertySelector(SavedSearch_.user);
            selector.add(null);
            SavedSearch publicExample = example(searchFrom).name(name);

            for (SavedSearch savedSearch : savedSearchRepository.find(publicExample, new SearchParameters().caseInsensitive().anywhere().property(selector))) {
                results.add(savedSearch);
            }
            return results;
        }

        private List<SavedSearch> findPrivate(String name) {
            List<SavedSearch> results = newArrayList();
            SavedSearch privateExample = privateExample(searchFrom).name(name);
            for (SavedSearch savedSearch : savedSearchRepository.find(privateExample, new SearchParameters().caseInsensitive().anywhere())) {
                results.add(savedSearch);
            }
            return results;
        }
    }
}