/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-selenium-primefaces:src/test/java/selenium/support/element/Link.p.vm.java
 */
package com.jaxio.web.selenium.support.elements;

public class Link extends CustomElement {
    public Link(String id) {
        super(id);
    }

    public String getText() {
        webClient.waitUntilDisplayed(by());
        return webClient.find(by()).getText();
    }

    public void click() {
        webClient.click(by());
    }
}