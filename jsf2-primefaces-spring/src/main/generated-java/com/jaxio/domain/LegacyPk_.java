/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/main/java/project/domain/CompositePk_.cpk.vm.java
 */
package com.jaxio.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(LegacyPk.class)
public abstract class LegacyPk_ {
    // pk attributes
    public static volatile SingularAttribute<LegacyPk, String> code;
    public static volatile SingularAttribute<LegacyPk, Integer> dept;
    public static volatile SingularAttribute<LegacyPk, String> name;
}
