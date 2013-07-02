/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-jsf2-spring-conversation:src/main/java/faces/CustomExceptionHandler.p.vm.java
 */
package com.jaxio.web.faces;

import java.util.Iterator;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.persistence.OptimisticLockException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

import com.jaxio.web.util.ExceptionUtil;
import com.jaxio.web.util.MessageUtil;

/**
 * Exception handling is configured here, in web.xml (see error-page tag) and in faces-config.xml.
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    /**
     * Construct a new exception handler around the given wrapped exception handler.
     * @param wrapped The wrapped exception handler.
     */
    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandledExceptionQueuedEvents.hasNext()) {
            Throwable e = unhandledExceptionQueuedEvents.next().getContext().getException();

            // map general purpose exception to error message
            if (ExceptionUtil.isCausedBy(e, OptimisticLockException.class)) {
                MessageUtil.getInstance().error("error_concurrent_modification");
                unhandledExceptionQueuedEvents.remove();
            } else if (ExceptionUtil.isCausedBy(e, DataIntegrityViolationException.class)) {
                MessageUtil.getInstance().error("error_data_integrity_violation");
                unhandledExceptionQueuedEvents.remove();
            } else if (ExceptionUtil.isCausedBy(e, AccessDeniedException.class)) {
                // works only if the spring security filter is before the exception filter, 
                // that is if the exception filter handles the exception first.
                MessageUtil.getInstance().error("error_access_denied");
                unhandledExceptionQueuedEvents.remove();
            }
            // exception will be handled by the wrapped exception handler.
        }

        wrapped.handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }
}
