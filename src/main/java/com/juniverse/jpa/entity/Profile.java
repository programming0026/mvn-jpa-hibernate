package com.juniverse.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Uno a uno (lado dueño): contiene la columna {@code user_id}, que además es única para que sea realmente 1 a 1.
 */
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    protected Profile() {
    }

    /**
     * Crea el perfil.
     *
     * @param bio biografía
     */
    public Profile(String bio) {
        this.bio = bio;
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
     * Biografía.
     *
     * @return texto
     */
    public String getBio() {
        return bio;
    }

    /**
     * Usuario dueño del perfil.
     *
     * @return usuario
     */
    public AppUser getUser() {
        return user;
    }

    void setUser(AppUser user) {
        this.user = user;
    }
}
