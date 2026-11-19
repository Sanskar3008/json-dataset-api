
# JSON Dataset API

A Spring Boot REST API for storing and querying dynamic JSON datasets using PostgreSQL JSONB.

## Overview

The application allows clients to:
- Insert JSON records into a named dataset.
- Retrieve all records from a dataset.
- Group records by a specified JSON field.
- Sort records by a specified JSON field in ascending or descending order.

All datasets share a common relational database table, while JSONB provides flexible storage for records with different structures.

## Technology Stack

- Java 17
- Spring Boot 3.5.16
- Spring Data JPA
- PostgreSQL
- JSONB
- Maven
- JUnit 5
- Mockito
- Springdoc OpenAPI / Swagger

## Architecture

The application follows a layered architecture:

- Controller: Handles HTTP requests and responses.
- Service: Implements insertion, grouping and sorting logic.
- Repository: Handles database operations.
- Entity: Maps dataset records to the database.
- DTO: Defines API response structures.
- Validation: Validates dataset names and query parameters.
- Exception: Centralizes API error handling.

## Database Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE json_dataset_db;
```

Configure `src/main/resources/application.properties`:

```properties
spring.application.name=json-dataset-api

spring.datasource.url=jdbc:postgresql://localhost:5432/json_dataset_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace `YOUR_PASSWORD` with your local PostgreSQL password.

## Run the Application

Clone the repository:

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
cd json-dataset-api
```

Build the application:

```bash
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

## API Endpoints

### 1. Insert a Record

**POST**

```text
/api/dataset/{datasetName}/record
```

Example request:

```json
{
  "name": "Amit",
  "age": 25,
  "department": "Engineering",
  "salary": 50000
}
```

Success response: `201 Created`

### 2. Retrieve All Records

**GET**

```text
/api/dataset/employees/query
```

Returns all records belonging to the employees dataset.

### 3. Group Records

**GET**

```text
/api/dataset/employees/query?groupBy=department
```

Groups records based on the department field.

### 4. Sort Records

Ascending:

```text
/api/dataset/employees/query?sortBy=age&order=asc
```

Descending:

```text
/api/dataset/employees/query?sortBy=salary&order=desc
```

### Query Rules

- `groupBy` and `sortBy` cannot be used together.
- Sort order supports `asc` and `desc`.
- If neither parameter is provided, all records are returned.
- Sorting supports numeric, string and boolean values.
- Missing and null values are placed last when sorting.
- Dataset names must start with a letter and contain only letters, numbers, hyphens or underscores.

## Swagger Documentation

Once the application is running, access:

```text
http://localhost:8080/swagger-ui/index.html
```

## Run Tests

Execute all automated tests:

```bash
mvn clean test
```

The project includes unit and controller tests using JUnit 5, Mockito and MockMvc.

## Error Handling

The API provides consistent error responses for:
- Invalid request parameters: `400 Bad Request`
- Invalid JSON request bodies: `400 Bad Request`
- Unknown endpoints: `404 Not Found`
- Unexpected server errors: `500 Internal Server Error`

