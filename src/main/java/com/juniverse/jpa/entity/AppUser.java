package com.juniverse.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Uno a uno (lado inverso): un usuario tiene un perfil. La clave foránea vive en la tabla del perfil.
 */
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    /** {@code mappedBy}: este lado NO tiene la columna; el dueño de la relación es {@code Profile.user}. */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    protected AppUser() {
    }

    /**
     * Crea el usuario.
     *
     * @param username nombre de usuario único
     */
    public AppUser(String username) {
        this.username = username;
    }

    /**
     * Asocia (o quita) el perfil manteniendo ambos lados sincronizados.
     *
     * @param profile perfil nuevo o {@code null} para quitarlo
     */
    public void setProfile(Profile profile) {
        if (this.profile != null) {
            this.profile.setUser(null);
        }
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
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
     * Nombre de usuario.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Perfil asociado.
     *
     * @return perfil o {@code null}
     */
    public Profile getProfile() {
        return profile;
    }
}
