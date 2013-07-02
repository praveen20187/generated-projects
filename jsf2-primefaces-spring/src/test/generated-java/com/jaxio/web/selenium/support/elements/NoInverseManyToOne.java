/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-selenium-primefaces:src/test/java/selenium/support/element/NoInverseManyToOne.p.vm.java
 */
package com.jaxio.web.selenium.support.elements;

import org.apache.commons.lang.WordUtils;

public class NoInverseManyToOne extends ManyToOne {
    public Button select;
    public Button edit;

    public NoInverseManyToOne(String id) {
        super(id);
        String capitalized = WordUtils.capitalize(id.split(":")[1]);
        select = new Button("form:select" + capitalized);
        edit = new Button("form:edit" + capitalized);
    }
}
