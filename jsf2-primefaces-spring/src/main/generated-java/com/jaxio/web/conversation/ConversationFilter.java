/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring:src/main/java/conversation/ConversationFilter.p.vm.java
 */
package com.jaxio.web.conversation;

import static com.jaxio.web.conversation.ConversationHolder.getCurrentConversation;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jaxio.context.LogContext;
import com.jaxio.context.UserContext;

/**
 * Filter responsible for creating/resuming {@link Conversation}.
 * By convention, the conversation id is carried by the _cid_ parameter.
 * To create a new conversation, you must request the initial conversation view and pass the _ncid_=value parameter.
 */
@Named
public class ConversationFilter implements Filter {
    private static final Logger log = Logger.getLogger(ConversationFilter.class);

    @Inject
    private ConversationManager conversationManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // set up log context for this thread so these information can be used by log4j
        String username = UserContext.getUsername();
        LogContext.setLogin(username != null ? username : "no username");
        LogContext.setSessionId(request.getSession().getId());

        String cid = request.getParameter("_cid_");

        if (cid != null) {
            // -----------------------------
            // RESUME existing conversation
            // -----------------------------
            try {
                conversationManager.resumeConversation(cid, request);
                if (log.isDebugEnabled()) {
                    log.debug("Conv. " + cid + " resumed. Nb ctx: " + getCurrentConversation().getConversationContextesCount() + ". Uri: "
                            + request.getRequestURI());
                }
            } catch (UnexpectedConversationException uue) {
                log.error(uue.getMessage());
                response.sendRedirect(request.getContextPath() + uue.getRedirectUrl());
                return;
            }

            try {
                filterChain.doFilter(request, response);
            } finally {
                conversationManager.pauseCurrentConversation();
            }
        } else if (!request.getRequestURI().contains("/javax.faces.resource/") && "true".equals(request.getParameter("_ncid_"))) {
            // -----------------------------
            // CREATE new conversation
            // -----------------------------

            // Limitation (per user) on the number of simultaneous conversations.
            if (conversationManager.isMaxConversationsReached(request.getSession())) {
                response.sendRedirect(request.getContextPath() + "/error/limit.faces");
                return;
            } else {
                // CREATE a new conversation
                try {
                    Conversation conversation = conversationManager.createConversation(request);
                    response.sendRedirect(request.getContextPath() + conversation.nextUrl());
                } catch (UnexpectedConversationException uue) {
                    log.error(uue.getMessage());
                    response.sendRedirect(request.getContextPath() + uue.getRedirectUrl());
                }
                return;
            }
        } else {
            // -----------------------------
            // Not related to conversations
            // -----------------------------
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}