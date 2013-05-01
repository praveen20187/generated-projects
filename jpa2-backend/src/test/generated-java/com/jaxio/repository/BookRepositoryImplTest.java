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

import com.jaxio.domain.Book;
import com.jaxio.repository.BookRepositoryImpl;
import com.jaxio.dao.BookDao;
import com.jaxio.dao.support.SearchParameters;

/**
 * Unit test on BookRepositoryImpl
 */
public class BookRepositoryImplTest {

    private BookRepositoryImpl bookRepositoryImpl;
    private BookDao bookDao;

    @Before
    public void setUp() {
        // called before each test.
        bookRepositoryImpl = new BookRepositoryImpl();
        bookDao = mock(BookDao.class);
        bookRepositoryImpl.setBookDao(bookDao);
    }

    @Test
    public void testFindUniqueOrNoneCaseNone() {
        Book none = null;

        when(bookDao.findUniqueOrNone(any(Book.class), any(SearchParameters.class))).thenReturn(none);

        Book result = bookRepositoryImpl.findUniqueOrNone(new Book());

        assertThat(result).isNull();
        verify(bookDao, times(1)).findUniqueOrNone(any(Book.class), any(SearchParameters.class));
    }

    @Test
    public void testFindUniqueOrNoneCaseUnique() {
        Book unique = new Book();

        when(bookDao.findUniqueOrNone(any(Book.class), any(SearchParameters.class))).thenReturn(unique);

        Book result = bookRepositoryImpl.findUniqueOrNone(new Book());

        assertThat(result).isNotNull();
        verify(bookDao, times(1)).findUniqueOrNone(any(Book.class), any(SearchParameters.class));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NonUniqueResultException.class)
    public void testFindUniqueOrNoneCaseMultiple() {
        when(bookDao.findUniqueOrNone(any(Book.class), any(SearchParameters.class))).thenThrow(NonUniqueResultException.class);

        bookRepositoryImpl.findUniqueOrNone(new Book());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NoResultException.class)
    public void testFindUniqueCaseNone() {
        when(bookDao.findUnique(any(Book.class), any(SearchParameters.class))).thenThrow(NoResultException.class);

        bookRepositoryImpl.findUnique(new Book());
    }

    @Test
    public void testFindUniqueCaseUnique() {
        Book unique = new Book();

        when(bookDao.findUnique(any(Book.class), any(SearchParameters.class))).thenReturn(unique);

        Book result = bookRepositoryImpl.findUnique(new Book());

        assertThat(result).isNotNull();
        verify(bookDao, times(1)).findUnique(any(Book.class), any(SearchParameters.class));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NonUniqueResultException.class)
    public void testFindUniqueCaseMultiple() {
        when(bookDao.findUnique(any(Book.class), any(SearchParameters.class))).thenThrow(NonUniqueResultException.class);

        bookRepositoryImpl.findUnique(new Book());
    }
}