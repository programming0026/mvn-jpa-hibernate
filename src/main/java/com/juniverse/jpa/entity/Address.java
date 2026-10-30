package com.juniverse.jpa.entity;

import jakarta.persistence.Embeddable;

/**
 * Objeto incrustado: sus campos se guardan como columnas de la tabla de la entidad que lo contiene (no tiene tabla propia).
 */
@Embeddable
public class Address {

    private String street;
    private String city;

    protected Address() {
    }

    /**
     * Crea la dirección.
     *
     * @param street calle
     * @param city ciudad
     */
    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    /**
     * Calle.
     *
     * @return calle
     */
    public String getStreet() {
        return street;
    }

    /**
     * Ciudad.
     *
     * @return ciudad
     */
    public String getCity() {
        return city;
    }
}
