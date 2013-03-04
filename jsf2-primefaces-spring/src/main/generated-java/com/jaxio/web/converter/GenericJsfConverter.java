/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-spring-conversation:src/main/java/converter/GenericJsfConverter.p.vm.java
 */
package com.jaxio.web.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.core.convert.support.DefaultConversionService;

import com.jaxio.domain.Identifiable;
import com.jaxio.repository.support.Repository;

/**
 * Base JSF converter for JPA entities.
 */
public class GenericJsfConverter<E extends Identifiable<PK>, PK extends Serializable> implements Converter {
    private Class<E> type;
    private Class<PK> pkType;

    protected DefaultConversionService conversionService = new DefaultConversionService();
    protected Repository<E, PK> entityService;

    protected GenericJsfConverter(Repository<E, PK> entityService, Class<E> type, Class<PK> pkType) {
        this.entityService = entityService;
        this.type = type;
        this.pkType = pkType;
    }

    public Class<E> getType() {
        return type;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (value == null || "-1".equals(value)) {
            return null;
        }

        return entityService.getById(toPk(value));
    }

    protected PK toPk(String value) {
        return conversionService.convert(value, pkType);
    }

    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null || !Identifiable.class.isAssignableFrom(object.getClass())) {
            return "-1";
        }
        return ((Identifiable<PK>) object).getId().toString();
    }
}
