package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.Product;
import com.juniverse.jpa.entity.PurchaseOrder;
import com.juniverse.jpa.util.JpaUtil;
import java.math.BigDecimal;
import java.util.List;

/** Muchos a muchos con atributos propios: pedidos y productos unidos por la entidad {@code OrderItem}. */
public final class AssociationEntityDemo {

    private AssociationEntityDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Long orderId = JpaUtil.txResult(em -> {
            Product teclado = new Product("Teclado", new BigDecimal("899.00"));
            Product raton = new Product("Ratón", new BigDecimal("349.50"));
            em.persist(teclado);
            em.persist(raton);
            PurchaseOrder order = new PurchaseOrder("Ana");
            order.addItem(teclado, 1);
            order.addItem(raton, 2);
            em.persist(order);
            return order.getId();
        });

        JpaUtil.tx(em -> {
            PurchaseOrder order = em.find(PurchaseOrder.class, orderId);
            order.getItems().forEach(i -> System.out.println("  " + i.getProduct().getName() + " x" + i.getQuantity()));
            System.out.println("Total calculado en Java: $" + order.total());
        });

        JpaUtil.tx(em -> {
            BigDecimal total = em.createQuery("select sum(i.quantity * i.unitPrice) from OrderItem i where i.order.id = :id", BigDecimal.class)
                    .setParameter("id", orderId).getSingleResult();
            System.out.println("Total calculado con JPQL (sum): $" + total);
            List<Object[]> porProducto = em.createQuery(
                    "select i.product.name, sum(i.quantity) from OrderItem i group by i.product.name order by i.product.name", Object[].class).getResultList();
            porProducto.forEach(f -> System.out.println("  Unidades vendidas de " + f[0] + ": " + f[1]));
        });
        System.out.println("Nota: el precio se guarda en la línea (unitPrice) para que un cambio posterior del catálogo no altere pedidos pasados.");
    }
}
