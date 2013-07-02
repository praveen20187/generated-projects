/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-backend-jpa:src/main/java/project/printer/Printer.e.vm.java
 */
package com.jaxio.printer;

import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import com.jaxio.domain.User;
import com.jaxio.domain.User_;
import com.jaxio.printer.support.GenericPrinter;

/**
 * {@link GenericPrinter} for {@link User} 
 *
 * @see TypeAwarePrinter
 */
@Named
@Singleton
public class UserPrinter extends GenericPrinter<User> {
    public UserPrinter() {
        super(User.class, User_.username.getName(), User_.firstName.getName(), User_.lastName.getName());
    }

    @Override
    public String print(User user, Locale locale) {
        if (user == null) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        appendIfNotEmpty(ret, user.getUsername());
        appendIfNotEmpty(ret, user.getFirstName());
        appendIfNotEmpty(ret, user.getLastName());
        return ret.toString();
    }
}