package com.juniverse.jpa.demo;

import com.juniverse.jpa.entity.AppUser;
import com.juniverse.jpa.entity.Profile;
import com.juniverse.jpa.util.JpaUtil;

import java.util.logging.Logger;
import com.juniverse.jpa.AppLogger;
/** Demostración de {@code @OneToOne}: cascada, consulta por el lado dueño y eliminación de huérfanos. */
public final class OneToOneDemo {
    private static final Logger LOGGER = AppLogger.get(OneToOneDemo.class);


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
        LOGGER.info("Usuarios: " + JpaUtil.countRows("app_users") + ", perfiles: " + JpaUtil.countRows("profile"));

        JpaUtil.tx(em -> {
            Profile profile = em.createQuery("select p from Profile p where p.user.username = :u", Profile.class)
                    .setParameter("u", "ana").getSingleResult();
            LOGGER.info("Consulta desde el lado dueño -> bio: " + profile.getBio());
        });

        JpaUtil.tx(em -> em.find(AppUser.class, userId).setProfile(null)); // orphanRemoval borra el perfil
        LOGGER.info("Tras quitar el perfil (orphanRemoval) -> perfiles: " + JpaUtil.countRows("profile"));
    }
}
