/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-selenium-primefaces:src/test/java/selenium/support/element/ManyEnums.p.vm.java
 */
package com.jaxio.web.selenium.support.elements;

import static com.google.common.collect.Lists.newArrayList;
import static com.palominolabs.xpath.XPathUtils.getXPathString;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ManyEnums<T extends Enum<T>> extends CustomWebElement {
    private final String xPathId;
    private final Class<T> clazz;

    public ManyEnums(Class<T> clazz, String id) {
        this.clazz = clazz;
        this.xPathId = getXPathString(id);
    }

    public boolean isSelected(T value) {
        return webClient.find(By.xpath("//input[@type='checkbox' and @name=" + xPathId + " and @value=" + getXPathString(value.name()) + "]")).isSelected();

    }

    public void select(T... values) {
        for (T value : values) {
            if (!isSelected(value)) {
                webClient.click(By.xpath("//input[@type='checkbox' and @name=" + xPathId + " and @value=" + getXPathString(value.name()) + "]"));
            }
        }
    }

    public List<T> values() {
        List<T> ret = newArrayList();
        for (WebElement webElement : webClient.findAll(By.xpath("//input[@type='checkbox' and @name=" + xPathId + "]"))) {
            if (webElement.isSelected()) {
                String attribute = webElement.getAttribute("value");
                ret.add(Enum.valueOf(clazz, attribute));
            }
        }
        return ret;
    }
}
