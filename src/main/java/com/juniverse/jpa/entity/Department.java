package com.juniverse.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Uno a muchos: un departamento tiene muchos empleados. El lado "uno" es el inverso ({@code mappedBy}).
 */
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** {@code orphanRemoval}: un empleado quitado de la lista se borra de la base de datos. */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    protected Department() {
    }

    /**
     * Crea el departamento.
     *
     * @param name nombre
     */
    public Department(String name) {
        this.name = name;
    }

    /**
     * Agrega un empleado manteniendo sincronizados los dos lados de la relación.
     *
     * @param employee empleado
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    /**
     * Quita un empleado (y, por orphanRemoval, lo elimina).
     *
     * @param employee empleado
     */
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
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
     * Empleados (carga perezosa).
     *
     * @return lista de empleados
     */
    public List<Employee> getEmployees() {
        return employees;
    }
}
