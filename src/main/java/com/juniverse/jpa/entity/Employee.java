package com.juniverse.jpa.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Muchos a uno (lado dueño): muchos empleados pertenecen a un departamento; la columna {@code department_id} está aquí.
 */
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** {@code LAZY}: el departamento no se consulta hasta que se usa (por defecto {@code ManyToOne} es EAGER). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Embedded
    private Address address;

    protected Employee() {
    }

    /**
     * Crea el empleado.
     *
     * @param name nombre
     * @param address dirección incrustada
     */
    public Employee(String name, Address address) {
        this.name = name;
        this.address = address;
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
     * Departamento.
     *
     * @return departamento
     */
    public Department getDepartment() {
        return department;
    }

    void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Dirección.
     *
     * @return dirección
     */
    public Address getAddress() {
        return address;
    }
}
