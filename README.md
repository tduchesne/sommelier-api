# Sommelier API

A Spring Boot REST API for wine and dish pairing recommendations.

## Prerequisites

- Java 17 or later
- Maven 3.9.6+
- PostgreSQL 12+ (for production and local development)
- Docker (optional, for containerized deployment)

## Local Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd sommelier-api
```

### 2. Configure Database Environment Variables

Create a local environment file or export the required variables:

**Option A: Using environment variables (recommended)**

```bash
export DB_HOST=localhost:5432
export DB_USER=postgres
export DB_PASSWORD=your_local_db_password
export SPRING_PROFILE=local
```

**Option B: Using `application.properties` locally**

```bash
cp src/main/resources/application.properties.sample src/main/resources/application.properties
# Edit application.properties with your local database credentials
```

⚠️ **Important:** `application.properties` is in `.gitignore` to prevent accidentally committing credentials. Use either environment variables or the sample file.

### 3. Configure the `application-local.properties` Profile

For development, create or edit `src/main/resources/application-local.properties`:

```properties
# Logging (verbose for development)
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=DEBUG
spring.jpa.show-sql=true

# Database auto-update (update schema on startup)
spring.jpa.hibernate.ddl-auto=update
```

### 4. Build and Run Locally

```bash
# Build the project
mvn clean package

# Run with local profile
java -Dspring.profiles.active=local -jar target/sommelier-api-0.0.1-SNAPSHOT.jar
```

Or run directly via Maven:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

The API will start on `http://localhost:8080`

## Production Deployment

### Environment Variables

For production deployment, set the following environment variables **securely** (using your hosting platform's secrets manager):

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host and port | `db.example.com:5432` |
| `DB_USER` | Database username | `sommelier_prod` |
| `DB_PASSWORD` | Database password | `secure_password_here` |
| `SPRING_PROFILE` | Spring profile to activate | `prod` (default) |

### Docker Deployment

Build and run the Docker image:

```bash
# Build the image
docker build -t sommelier-api:latest .

# Run with environment variables (e.g., on Render, Railway, or local Docker)
docker run \
  -e DB_HOST=db.example.com:5432 \
  -e DB_USER=sommelier_prod \
  -e DB_PASSWORD=secure_password \
  -e SPRING_PROFILE=prod \
  -p 8080:8080 \
  sommelier-api:latest
```

### Secrets Management Best Practices

1. **Never commit credentials** to version control
2. **Use environment variables** for sensitive data (database passwords, API keys, etc.)
3. **Use your platform's secrets manager**:
   - **Render**: Use environment variables in the dashboard
   - **Railway**: Use service variables
   - **Kubernetes**: Use Secrets objects
   - **Docker Compose**: Use `.env` files (add to `.gitignore`)
4. **Rotate credentials regularly** and audit access logs
5. **Use strong, randomly generated passwords** (minimum 16 characters)

### Production Configuration Profile

The `prod` profile is the default and includes:

- No SQL logging (production safety)
- No stack traces exposed to clients
- Database schema is **not** auto-updated (use migrations for schema changes)
- Error messages are sanitized

To explicitly use the prod profile:

```bash
java -Dspring.profiles.active=prod -jar target/sommelier-api-0.0.1-SNAPSHOT.jar
```

## Running Tests

```bash
# Run all tests with H2 in-memory database
mvn test

# Run tests with verbose output
mvn test -X
```

## Project Structure

```
src/
├── main/
│   ├── java/com/vinotech/sommelier_api/
│   │   ├── controller/          # REST endpoints
│   │   ├── service/             # Business logic
│   │   ├── model/               # JPA entities
│   │   ├── repository/          # Data access
│   │   └── exception/           # Exception handling
│   └── resources/
│       ├── application.properties          # Main config (env-based)
│       ├── application.properties.sample   # Template (commit this)
│       ├── application-local.properties    # Local dev profile
│       ├── application-prod.properties     # Prod profile
│       └── application-test.properties     # Test profile
└── test/
    └── java/com/vinotech/sommelier_api/   # Unit & integration tests
```

## Configuration Files

- **`application.properties`** (main, ignored in git): Environment-based configuration. Use environment variables: `DB_HOST`, `DB_USER`, `DB_PASSWORD`, `SPRING_PROFILE`
- **`application.properties.sample`** (template, committed): Use as a reference when setting up locally
- **`application-local.properties`** (local dev, ignored in git): Local-specific overrides (SQL logging, DDL auto-update)
- **`application-prod.properties`** (production profile): Production-specific settings
- **`application-test.properties`** (test profile): Test-specific settings (uses H2 in-memory database)

## API Endpoints

(Add your endpoints documentation here)

## License

(Add license information)

## Support

For issues or questions, please open an issue on GitHub or contact the development team.

