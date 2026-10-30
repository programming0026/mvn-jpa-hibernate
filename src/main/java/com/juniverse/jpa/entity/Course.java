package com.juniverse.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Muchos a muchos (lado inverso): no tiene tabla intermedia propia, usa la de {@code Student.courses}.
 */
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    protected Course() {
    }

    /**
     * Crea el curso.
     *
     * @param title título
     */
    public Course(String title) {
        this.title = title;
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
     * Título.
     *
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Estudiantes inscritos.
     *
     * @return conjunto de estudiantes
     */
    public Set<Student> getStudents() {
        return students;
    }
}
