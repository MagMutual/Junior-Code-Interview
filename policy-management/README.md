+ # Policy Management Service
+ 
+ A Spring Boot application for managing insurance policies, providing CRUD operations through a REST API.
+ 
+ ## Prerequisites
+ 
+ - Java 21
+ - Docker
+ - Gradle
+ 
+ ## Local Development Setup
+ 
+ ### 1. Start the Database
+ 
+ Run PostgreSQL using Docker:
+ 
+ ```bash
+ docker run --name policy-management-db \
+     -d \
+     -p 5432:5432 \
+     -e POSTGRES_USER=postgres \
+     -e POSTGRES_PASSWORD=postgres \
+     -e POSTGRES_DB=policy_management \
+     postgres
+ ```
+ 
+ ### 2. Configure Application
+ 
+ The application is configured to connect to PostgreSQL with these default settings in `src/main/resources/application.properties`:
+ 
+ ```properties
+ spring.datasource.url=jdbc:postgresql://localhost:5432/policy_management
+ spring.datasource.username=postgres
+ spring.datasource.password=postgres
+ ```
+ 
+ ### 3. Build and Run
+ 
+ ```bash
+ ./gradlew build
+ ./gradlew bootRun
+ ```
+ 
+ The application will start on `http://localhost:8080`
+ 
+ ## API Documentation
+ 
+ Once the application is running, you can access the OpenAPI documentation at:
+ - Swagger UI: `http://localhost:8080/swagger-ui.html`
+ - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
+ 
+ ## Available Endpoints
+ 
+ - `POST /api/policies` - Create a new policy
+ - `GET /api/policies/{id}` - Get a policy by ID
+ - `PUT /api/policies/{id}` - Update an existing policy
+ - `DELETE /api/policies/{id}` - Delete a policy
+ - `GET /api/policies` - List all policies
+ 
+ ## Testing
+ 
+ Run the test suite:
+ 
+ ```bash
+ ./gradlew test
+ ```
+ 
+ ## Database Management
+ 
+ ### Stop the Database
+ 
+ ```bash
+ docker stop policy-management-db
+ ```
+ 
+ ### Remove the Database Container
+ 
+ ```bash
+ docker rm policy-management-db
+ ```
+ 
+ ### View Database Logs
+ 
+ ```bash
+ docker logs policy-management-db
+ ```
+ 
+ ## Project Structure
+ 
+ - `controller/` - REST API endpoints
+ - `service/` - Business logic layer
+ - `repository/` - Data access layer
+ - `model/` - Domain entities
+ - `dto/` - Data transfer objects
+ - `exception/` - Custom exceptions
+ 
+ ## Technologies
+ 
+ - Spring Boot 3.4
+ - Spring Data JDBC
+ - PostgreSQL
+ - OpenAPI/Swagger
+ - JUnit 5
+ - Testcontainers
+ - Lombok 