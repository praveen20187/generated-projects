/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-backend-jpa:src/test/java/domain/ModelTest.e.vm.java
 */
package com.jaxio.domain;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static org.fest.assertions.Assertions.assertThat;

import java.io.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.jaxio.domain.Account;
import com.jaxio.domain.Currency;
import com.jaxio.util.*;

/**
 * Basic tests for Transaction
 */
@SuppressWarnings("unused")
public class TransactionTest {

    // test unique primary key
    @Test
    public void newInstanceHasNoPrimaryKey() {
        Transaction model = new Transaction();
        assertThat(model.isIdSet()).isFalse();
    }

    @Test
    public void isIdSetReturnsTrue() {
        Transaction model = new Transaction();
        model.setId(ValueGenerator.getUniqueInteger());
        assertThat(model.getId()).isNotNull();
        assertThat(model.isIdSet()).isTrue();
    }

    //-------------------------------------------------------------
    // Many to One:  Transaction.currency ==> Currency.id
    //-------------------------------------------------------------

    @Test
    public void manyToOne_setCurrency() {
        Transaction many = new Transaction();

        // init
        Currency one = new Currency();
        one.setId(ValueGenerator.getUniqueInteger());
        many.setCurrency(one);

        // make sure it is propagated properly
        assertThat(many.getCurrency()).isEqualTo(one);

        // now set it to back to null
        many.setCurrency(null);

        // make sure null is propagated properly
        assertThat(many.getCurrency()).isNull();
    }

    @Test
    public void manyToOne_setAccount() {
        Transaction many = new Transaction();

        // init
        Account one = new Account();
        one.setId(ValueGenerator.getUniqueInteger());
        many.setAccount(one);

        // make sure it is propagated properly
        assertThat(many.getAccount()).isEqualTo(one);

        // now set it to back to null
        many.setAccount(null);

        // make sure null is propagated properly
        assertThat(many.getAccount()).isNull();
    }

    /*
     public void equalsUsingPk() {
     Transaction model1 = new Transaction();
     Transaction model2 = new Transaction();

     Integer id = ValueGenerator.getUniqueInteger();
     model1.setId(id);
     model2.setId(id);

     model1.setAmount(BigDecimal.ONE);
     model2.setAmount(BigDecimal.ONE);

     model1.setTransactionDate(new org.joda.time.LocalDate());
     model2.setTransactionDate(new org.joda.time.LocalDate());

     model1.setValueDate(new org.joda.time.LocalDate());
     model2.setValueDate(new org.joda.time.LocalDate());

     model1.setDescription("a");
     model2.setDescription("a");

     model1.setVersion(1);
     model2.setVersion(1);
     assertThat(model1.isIdSet()).isTrue();
     assertThat(model2.isIdSet()).isTrue();
     assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
     assertThat(model1).isEqualTo(model2);
     assertThat(model2).isEqualTo(model1);
     }
     */
}