package com.juniverse.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Muchos a muchos (lado dueño): un estudiante cursa muchos cursos y un curso tiene muchos estudiantes.
 * Se guarda en una tabla intermedia {@code student_course}.
 */
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    protected Student() {
    }

    /**
     * Crea el estudiante.
     *
     * @param name nombre
     */
    public Student(String name) {
        this.name = name;
    }

    /**
     * Inscribe al estudiante en un curso (ambos lados).
     *
     * @param course curso
     */
    public void enroll(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    /**
     * Da de baja al estudiante de un curso (ambos lados).
     *
     * @param course curso
     */
    public void drop(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
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
     * Cursos.
     *
     * @return conjunto de cursos
     */
    public Set<Course> getCourses() {
        return courses;
    }
}
