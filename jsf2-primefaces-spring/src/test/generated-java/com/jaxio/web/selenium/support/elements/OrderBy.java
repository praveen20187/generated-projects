/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-selenium-primefaces:src/test/java/selenium/support/element/OrderBy.p.vm.java
 */
package com.jaxio.web.selenium.support.elements;

import static com.palominolabs.xpath.XPathUtils.getXPathString;

import javax.annotation.Nullable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

public class OrderBy extends CustomElement {
    public OrderBy(String id) {
        super(id);
    }

    private WebElement icon() {
        String xpath = "//tr/th[@id=" + getXPathString("form:searchResults:" + id) + "]/span[2]";
        return webClient.find(By.xpath(xpath));
    }

    public boolean containsClass(String cssClass) {
        return icon().getAttribute("class").contains(cssClass);
    }

    public boolean isUp() {
        return containsClass("ui-icon-triangle-1-n");
    }

    public boolean isDown() {
        return containsClass("ui-icon-triangle-1-s");
    }

    public boolean isUnsorted() {
        return !isUp() && !isDown();
    }

    public void up() {
        if (!isUp()) {
            icon().click();
            webClient.until(new Function<WebDriver, Boolean>() {
                @Override
                @Nullable
                public Boolean apply(WebDriver input) {
                    return isUp();
                }
            });
        }
    }

    public void down() {
        if (isDown()) {
            return;
        }
        if (isUnsorted()) {
            up();
        }
        webClient.click(icon());
        webClient.until(new Function<WebDriver, Boolean>() {
            @Override
            @Nullable
            public Boolean apply(WebDriver input) {
                return isDown();
            }
        });
    }
}
