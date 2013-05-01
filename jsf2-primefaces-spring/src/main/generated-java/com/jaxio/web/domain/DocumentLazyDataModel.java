/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring-conversation:src/main/java/domain/LazyDataModel.e.vm.java
 */
package com.jaxio.web.domain;

import javax.inject.Inject;
import javax.inject.Named;
import com.jaxio.domain.Document;
import com.jaxio.repository.DocumentRepository;
import com.jaxio.web.converter.DocumentJsfConverter;
import com.jaxio.web.domain.support.GenericLazyDataModel;
import com.jaxio.web.faces.Conversation;

@Named
@Conversation
public class DocumentLazyDataModel extends GenericLazyDataModel<Document, String, DocumentSearchForm> {
    private static final long serialVersionUID = 1L;

    @Inject
    public DocumentLazyDataModel(DocumentRepository documentRepository, DocumentJsfConverter documentConverter, DocumentController documentController,
            DocumentSearchForm documentSearchForm) {
        super(documentRepository, documentConverter, documentController, documentSearchForm);
    }
}