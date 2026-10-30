package com.juniverse.jpa;

import com.juniverse.jpa.demo.AssociationEntityDemo;
import com.juniverse.jpa.demo.FetchingDemo;
import com.juniverse.jpa.demo.ManyToManyDemo;
import com.juniverse.jpa.demo.OneToManyDemo;
import com.juniverse.jpa.demo.OneToOneDemo;
import com.juniverse.jpa.util.JpaUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JPA / Hibernate sin Spring y con H2 en memoria: relaciones uno a uno, uno a muchos, muchos a muchos,
 * entidad de asociación y estrategias de carga.
 */
public final class Main {

    /** Referencias fuertes: java.util.logging solo guarda referencias débiles y perdería la configuración. */
    private static final Logger[] QUIET = {
        Logger.getLogger("org.hibernate"),
        Logger.getLogger("org.jboss"),
        // Aviso de que el pool integrado no es para producción: aquí es un ejemplo con H2 en memoria
        Logger.getLogger("org.hibernate.orm.connections.pooling")
    };

    private Main() {
    }

    private static void title(String text) {
        System.out.println("\n=========== " + text + " ===========");
    }

    /**
     * Punto de entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        // Salida en UTF-8 y menos ruido del log de Hibernate
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        QUIET[0].setLevel(Level.WARNING);
        QUIET[1].setLevel(Level.WARNING);
        QUIET[2].setLevel(Level.SEVERE);
        try {
            title("1. UNO A UNO (@OneToOne)");
            OneToOneDemo.run();
            title("2. UNO A MUCHOS / MUCHOS A UNO (@OneToMany, @ManyToOne)");
            OneToManyDemo.run();
            title("3. MUCHOS A MUCHOS (@ManyToMany)");
            ManyToManyDemo.run();
            title("4. MUCHOS A MUCHOS CON ATRIBUTOS (entidad de asociación)");
            AssociationEntityDemo.run();
            title("5. ESTRATEGIAS DE CARGA: LAZY, N+1, JOIN FETCH y EntityGraph");
            FetchingDemo.run();
        } finally {
            JpaUtil.close();
        }
    }
}
