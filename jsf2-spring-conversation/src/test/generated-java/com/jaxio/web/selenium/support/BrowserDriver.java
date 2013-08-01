/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-selenium-primefaces:src/test/java/selenium/support/BrowserDriver.p.vm.java
 */
package com.jaxio.web.selenium.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public enum BrowserDriver {
    Firefox {
        @Override
        public WebDriver buildWebDriver() {
            return new FirefoxDriver();
        }
    }, //
    InternetExplorer {
        @Override
        public WebDriver buildWebDriver() {
            return new InternetExplorerDriver();
        }
    }, //
    Chrome {
        @Override
        public WebDriver buildWebDriver() {
            return new FirefoxDriver();
        }
    }, //
    Javascript {
        @Override
        public WebDriver buildWebDriver() {
            return new FirefoxDriver();
        }
    };
    public abstract WebDriver buildWebDriver();
}