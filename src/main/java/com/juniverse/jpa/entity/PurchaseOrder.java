package com.juniverse.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido. La relación muchos-a-muchos entre pedidos y productos que necesita datos propios (cantidad, precio al comprar)
 * se modela como DOS relaciones uno-a-muchos con una entidad intermedia: {@link OrderItem}.
 */
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected PurchaseOrder() {
    }

    /**
     * Crea el pedido.
     *
     * @param customer cliente
     */
    public PurchaseOrder(String customer) {
        this.customer = customer;
    }

    /**
     * Agrega una línea al pedido congelando el precio actual del producto.
     *
     * @param product producto
     * @param quantity cantidad
     */
    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(this, product, quantity, product.getPrice()));
    }

    /**
     * Total del pedido.
     *
     * @return suma de cantidad por precio unitario
     */
    public BigDecimal total() {
        return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
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
     * Cliente.
     *
     * @return cliente
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Líneas del pedido.
     *
     * @return lista de líneas
     */
    public List<OrderItem> getItems() {
        return items;
    }
}
