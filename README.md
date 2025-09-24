# Friends-Agenda
Aplicación monolítica (sin persistencia real) para gestionar Personas, Hobbys, Lugares y Eventos. El módulo ejecutable es `friends-events` (Spring Boot).

## Requisitos
- JDK 17 o superior compatible (Java 17+).

## Ejecutar la aplicación
Opción A (recomendada, usando el wrapper de Maven):
- Windows:
  1) `cd friends-events`
  2) `mvnw.cmd spring-boot:run`

- Linux/Mac:
  1) `cd friends-events`
  2) `./mvnw spring-boot:run`

Opción B (desde la raíz usando el POM del módulo):
- `mvn -f friends-events/pom.xml spring-boot:run`

La aplicación arranca en `http://localhost:8080`. La página de bienvenida está en `http://localhost:8080/` y muestra enlaces rápidos y ejemplos cURL.

## Endpoints principales
- `POST /api/lugares` – crea un lugar `{ direccion, ciudad }`
- `GET /api/lugares` – lista lugares
- `POST /api/personas` – crea una persona `{ nombre, fechaNac }` (fecha ISO: `YYYY-MM-DD`)
- `GET /api/personas` – lista personas
- `POST /api/eventos` – crea un evento `{ titulo, descripcion, inicio, fin, lugarId }` (fechas ISO: `YYYY-MM-DDThh:mm:ss`)
- `GET /api/eventos` – lista eventos
- `POST /api/eventos/{id}/participantes` – inscribe una persona al evento `{ personaId }`

## Ejemplos rápidos
Crear un lugar:
```
curl -X POST http://localhost:8080/api/lugares \
  -H "Content-Type: application/json" \
  -d '{
    "direccion": "Cra 1 #23-45",
    "ciudad": "Bogotá"
  }'
```

Crear una persona:
```
curl -X POST http://localhost:8080/api/personas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Alex",
    "fechaNac": "1990-05-20"
  }'
```

Crear un evento (usa un `lugarId` existente):
```
curl -X POST http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Kickoff",
    "descripcion": "Reunión inicial",
    "inicio": "2025-10-01T09:00:00",
    "fin": "2025-10-01T10:30:00",
    "lugarId": 1
  }'
```

Inscribir una persona en un evento:
```
curl -X POST http://localhost:8080/api/eventos/1/participantes \
  -H "Content-Type: application/json" \
  -d '{
    "personaId": 1
  }'
```

Listar eventos:
```
curl http://localhost:8080/api/eventos
```

## Notas
- Si ejecutas `mvn spring-boot:run` desde la raíz del repo (donde no hay `pom.xml`), Maven fallará. Usa `cd friends-events` o `-f friends-events/pom.xml`.
- También puedes usar el plugin con coordenadas completas: `mvn -f friends-events/pom.xml org.springframework.boot:spring-boot-maven-plugin:run`.
