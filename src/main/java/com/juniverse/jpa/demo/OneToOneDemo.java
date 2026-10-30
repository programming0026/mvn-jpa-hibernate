package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.AppUser;
import com.juniverse.jpa.entity.Profile;
import com.juniverse.jpa.util.JpaUtil;

/** Demostración de {@code @OneToOne}: cascada, consulta por el lado dueño y eliminación de huérfanos. */
public final class OneToOneDemo {

    private OneToOneDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Long userId = JpaUtil.txResult(em -> {
            AppUser user = new AppUser("ana");
            user.setProfile(new Profile("Desarrolladora Java"));
            em.persist(user); // el perfil se guarda solo por cascade = ALL
            return user.getId();
        });
        System.out.println("Usuarios: " + JpaUtil.countRows("app_users") + ", perfiles: " + JpaUtil.countRows("profile"));

        JpaUtil.tx(em -> {
            Profile profile = em.createQuery("select p from Profile p where p.user.username = :u", Profile.class)
                    .setParameter("u", "ana").getSingleResult();
            System.out.println("Consulta desde el lado dueño -> bio: " + profile.getBio());
        });

        JpaUtil.tx(em -> em.find(AppUser.class, userId).setProfile(null)); // orphanRemoval borra el perfil
        System.out.println("Tras quitar el perfil (orphanRemoval) -> perfiles: " + JpaUtil.countRows("profile"));
    }
}
