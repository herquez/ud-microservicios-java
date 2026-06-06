# UD-Microservicios Java

Spring Boot microservices course workspace. This repository currently contains one service: `companies-crud`, backed by PostgreSQL.

## Tech Stack

- Java 17
- Spring Boot 3.5.14
- Spring Web, Spring Data JPA, Spring Actuator
- PostgreSQL 16.1
- Maven (with Maven Wrapper)
- Docker Compose

## Project Structure

- `companies-crud/` — Spring Boot application module
- `compose.yaml` — local PostgreSQL setup
- `sql/create_schema.sql` — database schema
- `sql/data.sql` — seed data
- `recursos/` — course reference resources

## Prerequisites

- Java 17+
- Docker + Docker Compose

## Quick Start

1. Start the database:

```bash
docker compose up -d
```

2. Run the service:

```bash
./companies-crud/mvnw spring-boot:run
```

On Windows:

```bash
companies-crud\mvnw.cmd spring-boot:run
```

## Microservices Orchestration Script

Use `manage_services.py` from repository root to run all services in order and see logs in one terminal.

Service startup order:

1. `registry-server`
2. `config-server`
3. `report-ms`
4. `companies-crud`
5. `gateway`

Commands:

```bash
python manage_services.py start
python manage_services.py stop
python manage_services.py restart
python manage_services.py status
```

Behavior:

- `start` launches `db` (`docker compose up -d db`) and then starts all Spring Boot services in order.
- Logs are streamed in the same terminal with service prefixes, for example: `[gateway] ...`.
- `stop` stops all started Java processes and the DB container.
- `status` checks if the expected ports are reachable.

## Build and Test

From repository root:

```bash
./companies-crud/mvnw -f companies-crud/pom.xml clean package
./companies-crud/mvnw -f companies-crud/pom.xml test
```

On Windows:

```bash
companies-crud\mvnw.cmd -f companies-crud\pom.xml clean package
companies-crud\mvnw.cmd -f companies-crud\pom.xml test
```

## Database Configuration

Configured in `companies-crud/src/main/resources/application.properties`:

- URL: `jdbc:postgresql://localhost:5432/companies`
- User: `debuggeandoideas`
- Password: `udemy`

These match `compose.yaml` and are initialized automatically using the SQL files in `sql/`.

## Notes

- This appears to be an initial course stage. The project currently includes the Spring Boot bootstrap class and infrastructure setup.
- Actuator is included as a dependency for monitoring endpoints.
