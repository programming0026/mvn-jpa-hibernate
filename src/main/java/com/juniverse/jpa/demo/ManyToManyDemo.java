package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.Course;
import com.juniverse.jpa.entity.Student;
import com.juniverse.jpa.util.JpaUtil;
import java.util.List;

import java.util.logging.Logger;
import com.juniverse.jpa.AppLogger;
/** Demostración de {@code @ManyToMany} con tabla intermedia. */
public final class ManyToManyDemo {
    private static final Logger LOGGER = AppLogger.get(ManyToManyDemo.class);


    private ManyToManyDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        JpaUtil.tx(em -> {
            Course java = new Course("Java 21");
            Course sql = new Course("SQL");
            Student ana = new Student("Ana");
            Student beto = new Student("Beto");
            Student carla = new Student("Carla");
            ana.enroll(java);
            ana.enroll(sql);
            beto.enroll(java);
            carla.enroll(sql);
            em.persist(java);
            em.persist(sql);
            em.persist(ana);
            em.persist(beto);
            em.persist(carla);
        });
        LOGGER.info("Estudiantes: " + JpaUtil.countRows("student") + ", cursos: " + JpaUtil.countRows("course")
                + ", filas en la tabla intermedia student_course: " + JpaUtil.countRows("student_course"));

        JpaUtil.tx(em -> {
            List<String> names = em.createQuery(
                    "select s.name from Student s join s.courses c where c.title = :t order by s.name", String.class)
                    .setParameter("t", "Java 21").getResultList();
            LOGGER.info("Estudiantes de 'Java 21': " + names);
        });

        JpaUtil.tx(em -> {
            Student ana = em.createQuery("select s from Student s where s.name = 'Ana'", Student.class).getSingleResult();
            Course sql = em.createQuery("select c from Course c where c.title = 'SQL'", Course.class).getSingleResult();
            ana.drop(sql);
        });
        LOGGER.info("Tras dar de baja a Ana de SQL -> filas en student_course: " + JpaUtil.countRows("student_course")
                + " (estudiantes y cursos siguen existiendo)");
    }
}
