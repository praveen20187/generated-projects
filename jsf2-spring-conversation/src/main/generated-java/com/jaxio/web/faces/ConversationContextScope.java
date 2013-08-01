/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-jsf2-spring-conversation:src/main/java/faces/ConversationContextScope.p.vm.java
 */
package com.jaxio.web.faces;

import static com.jaxio.web.conversation.ConversationHolder.getCurrentConversation;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.jaxio.web.conversation.Conversation;
import com.jaxio.web.conversation.ConversationContext;

/**
 * Beans in the <code>conversationContext</code> scope reside in a {@link Conversation conversation}'s {@link ConversationContext}.
 * They are <code>visible</code> only when the conversation is bound to the current thread of execution and their 
 * hosting ConversationContext is on top of the conversation's contextes stack.
 * <p>
 * Such a design decision allows a conversation to have 2 <code>conversation scoped</code> beans with 
 * the same name (they just have to reside in 2 different ConversationContext).
 * This prevents bean name clash in complex navigation scenario within the same conversation.
 */
public class ConversationContextScope implements Scope {

    @Override
    public String getConversationId() {
        return getCurrentConversation().getCid();
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Conversation currentConversation = getCurrentConversation();
        Object bean = currentConversation.getCurrentContext().getBean(name, Object.class);

        if (bean == null) {
            bean = objectFactory.getObject();
            currentConversation.getCurrentContext().addBean(name, bean);
        }

        return bean;
    }

    @Override
    public Object remove(String name) {
        throw new UnsupportedOperationException("remove is done during conversation.end");
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // no ops
    }

    @Override
    public Object resolveContextualObject(String key) {
        return getCurrentConversation().getCurrentContext().getVar(key);
    }
}