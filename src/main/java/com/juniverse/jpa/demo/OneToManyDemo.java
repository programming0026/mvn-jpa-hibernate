package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.Address;
import com.juniverse.jpa.entity.Department;
import com.juniverse.jpa.entity.Employee;
import com.juniverse.jpa.util.JpaUtil;
import java.util.List;

/** Demostración de {@code @OneToMany} / {@code @ManyToOne}: cascada, orphanRemoval, JPQL y objeto incrustado. */
public final class OneToManyDemo {

    private OneToManyDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Long deptId = JpaUtil.txResult(em -> {
            Department it = new Department("Tecnología");
            it.addEmployee(new Employee("Luis", new Address("Reforma 10", "CDMX")));
            it.addEmployee(new Employee("María", new Address("Juárez 22", "Guadalajara")));
            it.addEmployee(new Employee("Pablo", new Address("Hidalgo 5", "Monterrey")));
            em.persist(it); // los tres empleados se guardan por cascade
            return it.getId();
        });
        System.out.println("Departamentos: " + JpaUtil.countRows("department") + ", empleados: " + JpaUtil.countRows("employee"));

        JpaUtil.tx(em -> {
            List<Object[]> filas = em.createQuery(
                    "select e.name, e.address.city from Employee e where e.department.name = :d order by e.name", Object[].class)
                    .setParameter("d", "Tecnología").getResultList();
            filas.forEach(f -> System.out.println("  " + f[0] + " vive en " + f[1] + " (columnas del objeto incrustado Address)"));
        });

        JpaUtil.tx(em -> {
            Department d = em.find(Department.class, deptId);
            Employee primero = d.getEmployees().get(0);
            d.removeEmployee(primero); // orphanRemoval = true -> DELETE
        });
        System.out.println("Tras quitar un empleado de la lista -> empleados: " + JpaUtil.countRows("employee"));

        JpaUtil.tx(em -> em.remove(em.find(Department.class, deptId))); // cascade REMOVE borra también a los empleados
        System.out.println("Tras borrar el departamento -> empleados: " + JpaUtil.countRows("employee"));
    }
}
