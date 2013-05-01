/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend-jpa:src/main/java/project/domain/Entity.e.vm.java
 */
package com.jaxio.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import com.google.common.base.Objects;
import com.jaxio.domain.Address_;
import com.jaxio.domain.IdentifiableHashBuilder;

@Entity
@Table(name = "ADDRESS")
public class Address implements Identifiable<Integer>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(Address.class);

    // Raw attributes
    private Integer id; // pk
    private String streetName;
    private String city; // not null
    private Integer version;

    // -------------------------------
    // Getter & Setter
    // -------------------------------
    // -- [id] ------------------------

    @Column(name = "ID", precision = 10)
    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Transient
    public boolean isIdSet() {
        return id != null;
    }

    // -- [streetName] ------------------------

    @Size(max = 100)
    @Column(name = "STREET_NAME", length = 100)
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    // -- [city] ------------------------

    @Size(max = 100)
    @NotEmpty
    @Column(name = "CITY", nullable = false, length = 100)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // -- [version] ------------------------

    @Column(name = "VERSION", precision = 10)
    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    }

    /**
     * equals implementation using a business key.
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof Address && hashCode() == other.hashCode());
    }

    private IdentifiableHashBuilder identifiableHashBuilder = new IdentifiableHashBuilder();

    @Override
    public int hashCode() {
        return identifiableHashBuilder.hash(log, this);
    }

    /**
     * Construct a readable string representation for this Address instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add(Address_.id.getName(), getId()) //
                .add(Address_.streetName.getName(), getStreetName()) //
                .add(Address_.city.getName(), getCity()) //
                .add(Address_.version.getName(), getVersion()) //
                .toString();
    }
}