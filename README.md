**Friends-Agenda – Ingesta y Normalización**

- Resumen: guarda payloads crudos en `raw_ingest` y normaliza a documento canónico `persons` en Mongo. Upsert por `canonicalId = sha256("person|" + email.toLowerCase())`. Validación JSON Schema con stub.

**Requisitos del Sistema**
- Sistema operativo: Linux, macOS o Windows 10/11.
- Docker Desktop (incluye Docker Engine y Compose Plugin).
  - Verifica con: `docker --version` y `docker compose version`.
- JDK 17 o superior (requerido para ejecutar/compilar la app).
  - Verifica con: `java -version` y que `JAVA_HOME` esté configurado.
- Maven 3.9+ opcional. No es obligatorio porque se usa el wrapper (`mvnw`).
- cURL para pruebas de endpoints.

**Puertos y Servicios**
- App Spring Boot: `http://localhost:8080`
- MongoDB: `localhost:27017` (autenticación en `admin` por root)
- Mongo Express: `http://localhost:8081`

**Archivos Clave**
- `docker-compose.yml`: orquesta MongoDB y Mongo Express.
- `.env`: credenciales para Mongo y Mongo Express.
- `friends-events/`: código de la aplicación Spring Boot.
  - Config: `friends-events/src/main/resources/application.yml`.
  - Esquema JSON: `friends-events/src/main/resources/json-schema/canonical-person.schema.json`.

**Preparación**
- Opcional: edita `.env` si deseas cambiar credenciales (por defecto: `admin/admin123`).
- Asegúrate de no tener puertos 27017/8081/8080 ocupados.

**1) Levantar MongoDB y Mongo Express**
- Opción Make: `make up`
- Opción Compose directa: `docker compose --env-file .env up -d`
- Verifica contenedores: `docker ps` (debe aparecer `fa_mongo` y `fa_mongo_express`).
- Panel: `http://localhost:8081` con `ME_USER`/`ME_PASS` desde `.env`.

**2) Compilar el proyecto (sin tests)**
- Linux/macOS:
  - `cd friends-events && ./mvnw -q -DskipTests package`
- Windows (PowerShell):
  - `cd friends-events; .\mvnw.cmd -q -DskipTests package`

**3) Ejecutar la aplicación**
- Linux/macOS:
  - `./mvnw spring-boot:run`
- Windows (PowerShell):
  - `.\mvnw.cmd spring-boot:run`
- Espera en logs: `Started AppEventsApplication` y escuchando en `:8080`.

**4) Probar Endpoints (cURL)**
- Endpoints expuestos:
  - `POST /api/ingest/raw?source=...` → guarda crudo en `raw_ingest` (202 Accepted, sin body).
  - `POST /api/ingest/person` → normaliza y upsert en `persons` (200 OK con JSON canónico).

- Ingesta cruda (Linux/macOS):
  - `curl -i -X POST "http://localhost:8080/api/ingest/raw?source=demo" -H "Content-Type: application/json" -d '{"nombre":"Ana","correo":"ana@mail.com","hobbies":["ciclismo"],"amigos":["carlos"],"ciudad":"Tunja"}'`
- Ingesta cruda (Windows PowerShell):
  - `curl -i -X POST "http://localhost:8080/api/ingest/raw?source=demo" -H "Content-Type: application/json" -d '{ "nombre": "Ana", "correo": "ana@mail.com", "hobbies": ["ciclismo"], "amigos": ["carlos"], "ciudad": "Tunja" }'`

- Normalización persona (Linux/macOS):
  - `curl -i -X POST "http://localhost:8080/api/ingest/person" -H "Content-Type: application/json" -d '{"nombre":"Ana","correo":"ana@mail.com","ciudad":"Tunja","source":"demo"}'`
- Normalización persona (Windows PowerShell):
  - `curl -i -X POST "http://localhost:8080/api/ingest/person" -H "Content-Type: application/json" -d '{ "nombre": "Ana", "correo": "ana@mail.com", "ciudad": "Tunja", "source": "demo" }'`

- Respuestas esperadas:
  - Ingesta cruda: `HTTP/1.1 202 Accepted`, sin contenido.
  - Normalización: `HTTP/1.1 200 OK` con JSON que incluye:
    - `canonicalId`: `sha256("person|" + email.toLowerCase())`.
      - Ejemplo para `ana@mail.com`: `83d1e0283716942ee632b3f1c786dc4fe7d79ae5a79ba7a739bbcddddee76294`.
    - `schemaVersion`: `1`
    - `name.full`: `"Ana"`
    - `emails[0].value`: `"ana@mail.com"`
    - `location.city`: `"Tunja"`, `location.country`: `"CO"`
    - `sourceMeta.source`: `"demo"`
    - `updatedAt`: timestamp ISO 8601
    - `id`: ObjectId asignado por Mongo

**5) Verificar en Mongo Express**
- Ir a `http://localhost:8081` (usa `ME_USER`/`ME_PASS` del `.env`).
- Base `friends_agenda`:
  - Colección `raw_ingest`: debe contener al menos 2 documentos de las semillas más tu POST crudo.
  - Colección `persons`: debe contener el documento canónico tras el POST de normalización.

**6) Apagar servicios**
- `docker compose down` (conserva datos)
- `docker compose down -v` (elimina volumen y datos)
- Alternativa Make: `make down` o `make reseed` (reinicia y recrea datos)

**Solución de Problemas**
- Docker no encontrado: instala Docker Desktop y activa la integración (WSL2 en Windows si aplica).
- JAVA_HOME no configurado: instala JDK 17 y configura `JAVA_HOME` al directorio del JDK.
- Puertos ocupados: cambia puertos o detén procesos en 8080/8081/27017.
- Conexión a Mongo falla: valida `application.yml` (`spring.data.mongodb.uri`) y credenciales del `.env`.

**Notas**
- La validación usa `json-schema/canonical-person.schema.json` y sólo verifica que el recurso exista (stub).
- No se modifican endpoints ajenos a esta fase.

**Mongo Express**
- URL: `http://localhost:8081`
- Credenciales: definidas en `.env` (`ME_USER` / `ME_PASS`)

**Seguridad**
- No expongas `mongo-express` ni MongoDB a Internet en producción. Restringe acceso por red/VPN y aplica usuarios/roles mínimos.

**Roadmap**
- Validación real de JSON Schema.
- Proyecciones a SQL/Neo4j.
