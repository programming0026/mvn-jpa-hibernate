package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.Address;
import com.juniverse.jpa.entity.Department;
import com.juniverse.jpa.entity.Employee;
import com.juniverse.jpa.util.JpaUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.hibernate.LazyInitializationException;

/**
 * Estrategias de carga: LAZY, el problema N+1 y cómo resolverlo con JOIN FETCH o un EntityGraph.
 */
public final class FetchingDemo {

    private FetchingDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        JpaUtil.tx(em -> {
            for (int d = 1; d <= 3; d++) {
                Department dep = new Department("Depto-" + d);
                dep.addEmployee(new Employee("Empleado " + d + "A", new Address("Calle " + d, "CDMX")));
                dep.addEmployee(new Employee("Empleado " + d + "B", new Address("Calle " + d, "CDMX")));
                em.persist(dep);
            }
        });

        // 1) N+1: 1 consulta para los departamentos + 1 por cada uno para sus empleados
        long antes = JpaUtil.statements();
        JpaUtil.tx(em -> {
            List<Department> deps = em.createQuery("select d from Department d where d.name like 'Depto-%'", Department.class).getResultList();
            deps.forEach(d -> d.getEmployees().size()); // cada acceso lanza una consulta
        });
        System.out.println("Carga perezosa (problema N+1): " + (JpaUtil.statements() - antes) + " sentencias SQL para 3 departamentos");

        // 2) JOIN FETCH: una sola consulta
        antes = JpaUtil.statements();
        JpaUtil.tx(em -> {
            List<Department> deps = em.createQuery(
                    "select distinct d from Department d join fetch d.employees where d.name like 'Depto-%'", Department.class).getResultList();
            deps.forEach(d -> d.getEmployees().size());
        });
        System.out.println("JOIN FETCH:                    " + (JpaUtil.statements() - antes) + " sentencia SQL");

        // 3) EntityGraph: lo mismo, declarado aparte de la consulta
        antes = JpaUtil.statements();
        JpaUtil.tx(em -> {
            EntityGraph<Department> graph = em.createEntityGraph(Department.class);
            graph.addSubgraph("employees");
            List<Department> deps = em.createQuery("select d from Department d where d.name like 'Depto-%'", Department.class)
                    .setHint("jakarta.persistence.fetchgraph", graph).getResultList();
            deps.forEach(d -> d.getEmployees().size());
        });
        System.out.println("EntityGraph:                   " + (JpaUtil.statements() - antes) + " sentencia SQL");

        // 4) LazyInitializationException: tocar una relación perezosa con el EntityManager ya cerrado
        EntityManager em = JpaUtil.open();
        Department detached = em.createQuery("select d from Department d where d.name = 'Depto-1'", Department.class).getSingleResult();
        em.close();
        try {
            detached.getEmployees().size();
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException: la relación se leyó con la sesión cerrada -> usa JOIN FETCH, EntityGraph o carga dentro de la transacción");
        }
    }
}
