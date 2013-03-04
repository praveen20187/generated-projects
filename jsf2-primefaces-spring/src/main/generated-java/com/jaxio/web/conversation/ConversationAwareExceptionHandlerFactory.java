/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring-conversation:src/main/java/conversation/ConversationAwareExceptionHandlerFactory.p.vm.java
 */
package com.jaxio.web.conversation;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class ConversationAwareExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory wrapped;

    /**
     * Construct a new full conversation aware exception handler factory around the given wrapped factory.
     * @param wrapped The wrapped factory.
     */
    public ConversationAwareExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Returns a new instance of {@link ConversationAwareExceptionHandler} which wraps the original exception handler.
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new ConversationAwareExceptionHandler(wrapped.getExceptionHandler());
    }

    /**
     * Returns the wrapped factory.
     */
    @Override
    public ExceptionHandlerFactory getWrapped() {
        return wrapped;
    }
}