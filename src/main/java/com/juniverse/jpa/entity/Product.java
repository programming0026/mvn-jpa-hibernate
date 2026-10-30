package com.juniverse.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

/** Producto del catálogo. */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    protected Product() {
    }

    /**
     * Crea el producto.
     *
     * @param name nombre
     * @param price precio actual
     */
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Identificador.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Nombre.
     *
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Precio actual.
     *
     * @return precio
     */
    public BigDecimal getPrice() {
        return price;
    }
}
