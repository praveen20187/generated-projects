/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/test/java/domain/ModelTest.e.vm.java
 */
package com.jaxio.domain.more;

import java.io.*;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;

import com.jaxio.util.*;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import com.jaxio.domain.Identifiable;

/**
 * Basic tests for MoreTypesDemo
 */
@SuppressWarnings("unused")
public class MoreTypesDemoTest {

    // test unique primary key
    @Test
    public void newInstanceHasNoPrimaryKey() {
        MoreTypesDemo model = new MoreTypesDemo();
        assertThat(model.isIdSet()).isFalse();
    }

    @Test
    public void isIdSetReturnsTrue() {
        MoreTypesDemo model = new MoreTypesDemo();
        model.setId(ValueGenerator.getUniqueInteger());
        assertThat(model.getId()).isNotNull();
        assertThat(model.isIdSet()).isTrue();
    }

    // test columns methods

    @Test
    public void toStringNotNull() {
        MoreTypesDemo model = new MoreTypesDemo();
        assertThat(model.toString()).isNotNull();
    }

    @Test
    public void equalsUsingPk() {
        MoreTypesDemo model1 = new MoreTypesDemo();
        MoreTypesDemo model2 = new MoreTypesDemo();

        Integer id = ValueGenerator.getUniqueInteger();
        model1.setId(id);
        model2.setId(id);

        model1.setNumberInt(1);
        model2.setNumberInt(1);

        model1.setNumberLong(1l);
        model2.setNumberLong(1l);

        model1.setNumberDouble(1d);
        model2.setNumberDouble(1d);

        model1.setNumberFloat(1f);
        model2.setNumberFloat(1f);

        model1.setNumberBigInteger(BigInteger.ONE);
        model2.setNumberBigInteger(BigInteger.ONE);

        model1.setNumberBigDecimal(BigDecimal.ONE);
        model2.setNumberBigDecimal(BigDecimal.ONE);

        model1.setDateJavaTemporalDate(new Date());
        model2.setDateJavaTemporalDate(new Date());

        model1.setDateJavaTemporalTimestamp(new Date());
        model2.setDateJavaTemporalTimestamp(new Date());

        model1.setDateJoda(new org.joda.time.LocalDate());
        model2.setDateJoda(new org.joda.time.LocalDate());

        model1.setDateTimeJoda(new org.joda.time.LocalDateTime());
        model2.setDateTimeJoda(new org.joda.time.LocalDateTime());

        model1.setVersion(1);
        model2.setVersion(1);
        assertThat(model1.isIdSet()).isTrue();
        assertThat(model2.isIdSet()).isTrue();
        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
        assertThat(model1).isEqualTo(model2);
        assertThat(model2).isEqualTo(model1);
    }
}