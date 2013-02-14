/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring:src/main/java/domain/Controller.e.vm.java
 */
package com.jaxio.web.domain;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.jaxio.dao.support.SearchParameters;
import com.jaxio.domain.Document;
import com.jaxio.repository.DocumentRepository;
import com.jaxio.web.conversation.Conversation;
import com.jaxio.web.conversation.ConversationContext;
import com.jaxio.web.conversation.ConversationFactory;

/**
 * Stateless controller for Document conversation start. Provides also auto-complete support. 
 */
@Named
@Singleton
public class DocumentController implements ConversationFactory {
    public final static String editUri = "/domain/documentEdit.faces";
    public final static String selectUri = "/domain/documentSelect.faces";
    private DocumentRepository documentRepository;

    @Inject
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    // --------------------------------
    // ConversationFactoryImpl
    // --------------------------------

    @Override
    public boolean canCreateConversation(HttpServletRequest request) {
        return selectUri.equals(request.getServletPath());
    }

    @Override
    public Conversation createConversation(HttpServletRequest request) {
        String uri = request.getServletPath();
        if (selectUri.equals(uri)) {
            Conversation conversation = Conversation.newInstance(request);
            ConversationContext<Document> ctx = newSearchContext();
            ctx.setLabelWithKey("document");
            conversation.setNextContext(ctx);
            return conversation;
        }

        throw new IllegalStateException("Unexpected conversation creation demand");
    }

    // --------------------------------
    // Autocomplete support
    // --------------------------------

    /**
     * This method is used from primefaces autocomplete components.
     */
    public List<Document> complete(String value) {
        SearchParameters sp = new SearchParameters().anywhere().searchPattern(value);
        return documentRepository.find(sp);
    }

    // --------------------------------
    // Upload/Download support 
    // --------------------------------    

    public DocumentUploadHandler getUploadHandler(Document document) {
        return new DocumentUploadHandler(document);
    }

    /**
     * Primefaces support for documentBinary file download
     */
    public StreamedContent getStreamedContent(Document document) {
        return new DefaultStreamedContent(new ByteArrayInputStream(document.getDocumentBinary()), document.getDocumentContentType(), document
                .getDocumentFileName());
    }

    // --------------------------------
    // Helper 
    // --------------------------------    

    /**
     * Helper to construct a new ConversationContext to edit an Document.
     * @param document the entity to edit.
     */
    public static ConversationContext<Document> newEditContext(final Document document) {
        ConversationContext<Document> ctx = new ConversationContext<Document>();
        ctx.setEntity(document); // used by GenericEditForm.init()
        ctx.setViewUri(editUri);
        ctx.addSourceIgnoringUseConversationEntityManager("form:account");
        return ctx;
    }

    /**
     * Helper to construct a new ConversationContext to edit an Document.
     * @param id the id of the entity to edit.
     */
    public static ConversationContext<Document> newEditContext(final String id) {
        ConversationContext<Document> ctx = new ConversationContext<Document>();
        ctx.setEntityId(id); // used by GenericEditForm.init()
        ctx.setViewUri(editUri);
        ctx.addSourceIgnoringUseConversationEntityManager("form:account");
        return ctx;
    }

    /**
     * Helper to construct a new ConversationContext for search/selection.
     */
    public static ConversationContext<Document> newSearchContext() {
        ConversationContext<Document> ctx = new ConversationContext<Document>();
        ctx.setUseConversationEntityManager(false);
        ctx.setViewUri(selectUri);
        return ctx;
    }
}