/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/main/java/project/printer/Printer.e.vm.java
 */
package com.jaxio.printer;

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.Locale;

import com.jaxio.domain.Account;
import com.jaxio.printer.DiscoverablePrinter;

/**
 * {@link org.springframework.format.Printer} for {@link Account} 
 *
 * @see org.springframework.format.Printer
 * @see DiscoverablePrinter
 * @see TypeAwarePrinter
 */
@Named
@Singleton
public class AccountPrinter extends DiscoverablePrinter<Account> {
    public AccountPrinter() {
        super(Account.class);
    }

    @Override
    public String print(Account account, Locale locale) {
        if (account == null) {
            return "";
        }
        StringBuilder ret = new StringBuilder(256);
        appendIfNotEmpty(ret, account.getUsername());
        appendIfNotEmpty(ret, account.getFirstName());
        appendIfNotEmpty(ret, account.getLastName());
        return ret.toString();
    }
}
