# Kotlin Api Spring Boot

REST API demo: Spring Boot 4, Kotlin 2.2, JPA (PostgreSQL), JWT (access + refresh), SpringDoc OpenAPI.

---

## Stack

| Layer        | Technology                          |
|-------------|-------------------------------------|
| Runtime     | Java 17, Kotlin 2.2.21              |
| Framework   | Spring Boot 4.0.2                   |
| Data        | Spring Data JPA, PostgreSQL 16      |
| Security    | Spring Security, JJWT 0.12 (HS256)  |
| Docs        | SpringDoc OpenAPI 2.8, Swagger UI    |
| Build       | Gradle (Kotlin DSL)                 |

---

## Requirements

- JDK 17+
- PostgreSQL (local or via Docker)
- (Optional) Docker and Docker Compose for containerized run

---

## Configuration

Datasource and JWT are driven by environment variables with defaults for local dev:

| Variable                    | Default                              | Description        |
|----------------------------|--------------------------------------|--------------------|
| `SPRING_DATASOURCE_URL`    | `jdbc:postgresql://localhost:5438/spring_base` | JDBC URL           |
| `SPRING_DATASOURCE_USERNAME` | `postgres`                         | DB user            |
| `SPRING_DATASOURCE_PASSWORD` | `postgres`                         | DB password        |
| `JWT_SECRET`               | (dev default in `application.properties`) | Base64 secret; decode to ≥32 bytes for HS256 |

Override in production; set `JWT_SECRET` to a strong, secret value.

---

## Run

**Local (DB must be reachable at configured URL):**

```bash
./gradlew bootRun
```

**With Docker Compose (PostgreSQL + app):**

```bash
docker compose up --build
```

- DB: port `5438` (host) → `5432` (container), database `spring_base`, user/password `postgres`.
- App: `http://localhost:8080`.

---

## API

- **Base URL:** `http://localhost:8080`
- **OpenAPI JSON:** `GET /v3/api-docs`
- **Swagger UI:** `GET /swagger-ui.html`

Auth: Bearer token (access token) in `Authorization` header. Register/login and refresh endpoints return access + refresh tokens; use refresh token at the refresh endpoint to obtain a new pair.

---

## Project layout

```
src/main/kotlin/.../demo/
├── api/              # DTOs, current-user helpers, API error model
├── config/           # JWT config, OpenAPI config
├── controllers/      # Auth, Notes, Status
├── database/
│   ├── model/        # JPA entities (User, Note, RefreshToken)
│   └── repository/   # JpaRepository interfaces
├── security/         # AuthService, JWT filter/service, encoder, SecurityConfig
├── GlobalValidationHandler.kt
└── KotlinApiSpringBootApplication.kt
```

---

## Tests

```bash
./gradlew test
```
