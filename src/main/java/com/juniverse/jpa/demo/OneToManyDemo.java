package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.Address;
import com.juniverse.jpa.entity.Department;
import com.juniverse.jpa.entity.Employee;
import com.juniverse.jpa.util.JpaUtil;
import java.util.List;

import java.util.logging.Logger;
import com.juniverse.jpa.AppLogger;
/** Demostración de {@code @OneToMany} / {@code @ManyToOne}: cascada, orphanRemoval, JPQL y objeto incrustado. */
public final class OneToManyDemo {
    private static final Logger LOGGER = AppLogger.get(OneToManyDemo.class);


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
        LOGGER.info("Departamentos: " + JpaUtil.countRows("department") + ", empleados: " + JpaUtil.countRows("employee"));

        JpaUtil.tx(em -> {
            List<Object[]> rows = em.createQuery(
                    "select e.name, e.address.city from Employee e where e.department.name = :d order by e.name", Object[].class)
                    .setParameter("d", "Tecnología").getResultList();
            rows.forEach(f -> LOGGER.info("  " + f[0] + " vive en " + f[1] + " (columnas del objeto incrustado Address)"));
        });

        JpaUtil.tx(em -> {
            Department d = em.find(Department.class, deptId);
            Employee first = d.getEmployees().get(0);
            d.removeEmployee(first); // orphanRemoval = true -> DELETE
        });
        LOGGER.info("Tras quitar un empleado de la lista -> empleados: " + JpaUtil.countRows("employee"));

        JpaUtil.tx(em -> em.remove(em.find(Department.class, deptId))); // cascade REMOVE borra también a los empleados
        LOGGER.info("Tras borrar el departamento -> empleados: " + JpaUtil.countRows("employee"));
    }
}
