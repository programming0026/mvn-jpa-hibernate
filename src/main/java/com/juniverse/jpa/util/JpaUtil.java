package com.juniverse.jpa.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.SessionFactory;

/**
 * Utilidades para trabajar con JPA sin un contenedor (Java SE): fábrica de EntityManager y manejo de transacciones.
 */
public final class JpaUtil {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("demo");

    private JpaUtil() {
    }

    /**
     * Crea un EntityManager (una "sesión" de trabajo). Hay que cerrarlo.
     *
     * @return nuevo EntityManager
     */
    public static EntityManager open() {
        return FACTORY.createEntityManager();
    }

    /**
     * Ejecuta un trabajo dentro de una transacción y devuelve un resultado (se llama {@code txResult} para no ser ambiguo con {@link #tx(Consumer)}). Si falla, hace rollback.
     *
     * @param work trabajo a ejecutar
     * @param <R> tipo del resultado
     * @return resultado del trabajo
     */
    public static <R> R txResult(Function<EntityManager, R> work) {
        try (EntityManager em = FACTORY.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                R result = work.apply(em);
                tx.commit();
                return result;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    /**
     * Ejecuta un trabajo sin resultado dentro de una transacción.
     *
     * @param work trabajo a ejecutar
     */
    public static void tx(Consumer<EntityManager> work) {
        txResult(em -> {
            work.accept(em);
            return null;
        });
    }

    /**
     * Número de sentencias SQL ejecutadas hasta ahora (estadísticas de Hibernate). Sirve para detectar el problema N+1.
     *
     * @return sentencias preparadas
     */
    public static long statements() {
        return FACTORY.unwrap(SessionFactory.class).getStatistics().getPrepareStatementCount();
    }

    /**
     * Cuenta filas de una tabla con SQL nativo.
     *
     * @param table nombre de la tabla
     * @return cantidad de filas
     */
    public static long countRows(String table) {
        return txResult(em -> ((Number) em.createNativeQuery("select count(*) from " + table).getSingleResult()).longValue());
    }

    /** Cierra la fábrica (libera la base de datos en memoria). */
    public static void close() {
        FACTORY.close();
    }
}
