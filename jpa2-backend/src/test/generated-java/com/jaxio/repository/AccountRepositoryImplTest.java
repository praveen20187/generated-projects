/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/test/java/service/ServiceImplTest.e.vm.java
 */
package com.jaxio.repository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.jaxio.domain.Account;
import com.jaxio.repository.AccountRepositoryImpl;
import com.jaxio.dao.AccountDao;
import com.jaxio.domain.Civility;
import com.jaxio.dao.support.SearchParameters;

/**
 * Unit test on AccountRepositoryImpl
 */
public class AccountRepositoryImplTest {

    private AccountRepositoryImpl accountRepositoryImpl;
    private AccountDao accountDao;

    @Before
    public void setUp() {
        // called before each test.
        accountRepositoryImpl = new AccountRepositoryImpl();
        accountDao = mock(AccountDao.class);
        accountRepositoryImpl.setAccountDao(accountDao);
    }

    @Test
    public void testFindUniqueOrNoneCaseNone() {
        Account none = null;

        when(accountDao.findUniqueOrNone(any(Account.class), any(SearchParameters.class))).thenReturn(none);

        Account result = accountRepositoryImpl.findUniqueOrNone(new Account());

        assertThat(result).isNull();
        verify(accountDao, times(1)).findUniqueOrNone(any(Account.class), any(SearchParameters.class));
    }

    @Test
    public void testFindUniqueOrNoneCaseUnique() {
        Account unique = new Account();

        when(accountDao.findUniqueOrNone(any(Account.class), any(SearchParameters.class))).thenReturn(unique);

        Account result = accountRepositoryImpl.findUniqueOrNone(new Account());

        assertThat(result).isNotNull();
        verify(accountDao, times(1)).findUniqueOrNone(any(Account.class), any(SearchParameters.class));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NonUniqueResultException.class)
    public void testFindUniqueOrNoneCaseMultiple() {
        when(accountDao.findUniqueOrNone(any(Account.class), any(SearchParameters.class))).thenThrow(NonUniqueResultException.class);

        accountRepositoryImpl.findUniqueOrNone(new Account());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NoResultException.class)
    public void testFindUniqueCaseNone() {
        when(accountDao.findUnique(any(Account.class), any(SearchParameters.class))).thenThrow(NoResultException.class);

        accountRepositoryImpl.findUnique(new Account());
    }

    @Test
    public void testFindUniqueCaseUnique() {
        Account unique = new Account();

        when(accountDao.findUnique(any(Account.class), any(SearchParameters.class))).thenReturn(unique);

        Account result = accountRepositoryImpl.findUnique(new Account());

        assertThat(result).isNotNull();
        verify(accountDao, times(1)).findUnique(any(Account.class), any(SearchParameters.class));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NonUniqueResultException.class)
    public void testFindUniqueCaseMultiple() {
        when(accountDao.findUnique(any(Account.class), any(SearchParameters.class))).thenThrow(NonUniqueResultException.class);

        accountRepositoryImpl.findUnique(new Account());
    }
}