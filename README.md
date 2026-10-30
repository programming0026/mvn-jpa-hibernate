# JPA / Hibernate: relaciones entre entidades

Ejemplo ejecutable de **JPA con Hibernate 6** (sin Spring) y **H2 en memoria**, centrado en las relaciones entre entidades y en cómo cargarlas bien. Java 21.

## Relaciones incluidas

| Relación | Entidades | Detalles |
|----------|-----------|----------|
| **Uno a uno** (`@OneToOne`) | `AppUser` ↔ `Profile` | Lado dueño con `@JoinColumn(unique = true)`, lado inverso con `mappedBy`, `cascade` y `orphanRemoval` |
| **Uno a muchos / muchos a uno** | `Department` ↔ `Employee` | Relación bidireccional con métodos que sincronizan ambos lados, `orphanRemoval`, cascada de borrado y objeto incrustado `@Embeddable` (`Address`) |
| **Muchos a muchos** (`@ManyToMany`) | `Student` ↔ `Course` | Tabla intermedia `student_course` con `@JoinTable`; el lado inverso usa `mappedBy` |
| **Muchos a muchos con atributos** | `PurchaseOrder` ↔ `Product` mediante `OrderItem` | Entidad de asociación (dos `@ManyToOne` + cantidad y precio unitario) |
| **Estrategias de carga** | `Department` → `Employee` | `LAZY`, problema **N+1**, `JOIN FETCH`, `EntityGraph` y `LazyInitializationException` |

## Estructura

```text
src/main/java/com/juniverse/jpa/
├── Main.java
├── entity/    # AppUser, Profile, Address, Department, Employee, Student, Course, Product, PurchaseOrder, OrderItem
├── demo/      # una demostración por tipo de relación y FetchingDemo
└── util/      # JpaUtil: EntityManagerFactory, transacciones y estadísticas
src/main/resources/META-INF/persistence.xml   # unidad de persistencia con H2
```

## Reglas prácticas

- Declara siempre el lado **dueño** (el que tiene la clave foránea) y usa `mappedBy` en el otro.
- Mantén sincronizados los dos lados con métodos como `addEmployee` / `enroll`.
- `@ManyToOne` y `@OneToOne` son `EAGER` por defecto: ponlos en `LAZY` y carga lo que necesites con `JOIN FETCH` o `EntityGraph`.
- Usa `cascade` y `orphanRemoval` solo en relaciones de **composición** (el hijo no tiene sentido sin el padre).
- Para `@ManyToMany` con datos extra, crea una **entidad de asociación**.
- Vigila el problema N+1: el programa cuenta las sentencias SQL para que lo veas.

## Requisitos

- JDK 21 y Maven 3.9 o superior. No hace falta instalar ninguna base de datos (H2 va incluida).

## Compilar y ejecutar

```bash
mvn compile exec:java

# o como JAR ejecutable (fat jar)
mvn clean package
java -jar target/mvn-jpa-hibernate-1.0.0-SNAPSHOT-launcher.jar
```

Para ver el SQL que genera Hibernate pon `hibernate.show_sql` en `true` en `persistence.xml`.

## Licencia

Código de ejemplo de uso libre con fines educativos.
