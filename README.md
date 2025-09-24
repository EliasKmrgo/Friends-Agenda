# Friends-Agenda
Aplicación monolítica que centraliza tres bases de datos (SQL, Neo4j, MongoDB) para el control de Personas, Hobbys y Eventos.

## Cómo ejecutar

Este repositorio contiene el módulo `friends-events`, que es una aplicación Spring Boot con su propio `pom.xml` y wrapper de Maven.

- Requisitos: `JDK 17` (o superior compatible).

Opciones de ejecución:

- Opción A (recomendada, usando el wrapper y desde el módulo):
  1. `cd friends-events`
  2. Windows: `mvnw.cmd spring-boot:run`
     Linux/Mac: `./mvnw spring-boot:run`

- Opción B (desde la raíz del repo, especificando el POM del módulo):
  - Windows: `mvn -f friends-events/pom.xml spring-boot:run`
  - Linux/Mac: `mvn -f friends-events/pom.xml spring-boot:run`

Notas:
- Si ejecutas `mvn spring-boot:run` desde la raíz (donde no hay `pom.xml`), Maven no encontrará el prefijo `spring-boot` y fallará. Asegúrate de correr los comandos dentro de `friends-events` o usa `-f friends-events/pom.xml`.
- También puedes ejecutar el plugin con coordenadas completas si lo prefieres: `mvn org.springframework.boot:spring-boot-maven-plugin:run -f friends-events/pom.xml`.
