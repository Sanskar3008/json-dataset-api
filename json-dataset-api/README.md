
# JSON Dataset API

A Spring Boot REST API for storing, retrieving, grouping, and sorting dynamic JSON datasets using PostgreSQL JSONB.

## Live Application

**Application URL:** https://json-dataset-api-somw.onrender.com

**Swagger Documentation:** https://json-dataset-api-somw.onrender.com/swagger-ui/index.html

The application is deployed on Render and uses a hosted PostgreSQL database.

## Overview

The JSON Dataset API allows clients to:

- Insert dynamic JSON records into named datasets.
- Retrieve all records belonging to a dataset.
- Group records by a specified JSON field.
- Sort records by a specified JSON field in ascending or descending order.
- Store records with different JSON structures in a shared PostgreSQL table.

PostgreSQL JSONB provides flexible JSON storage while maintaining relational database capabilities.

## Technology Stack

- Java 17
- Spring Boot 3.5.16
- Spring Data JPA
- PostgreSQL
- PostgreSQL JSONB
- Maven
- JUnit 5
- Mockito
- MockMvc
- Springdoc OpenAPI / Swagger
- Docker
- Render

## Architecture

The application follows a layered architecture.

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and responses |
| Service | Implements record insertion, grouping, and sorting logic |
| Repository | Handles database operations using Spring Data JPA |
| Entity | Maps dataset records to database tables |
| DTO | Defines API request and response structures |
| Validation | Validates dataset names and query parameters |
| Exception Handling | Centralizes API error handling |

### Database Design

All datasets share a common PostgreSQL table named `dataset_records`.

| Column | Description |
|---|---|
| id | Unique record identifier |
| dataset_name | Name of the dataset |
| record_data | JSON record stored as JSONB |
| created_at | Record creation timestamp |

An index on `dataset_name` supports efficient dataset retrieval.

## API Endpoints

### 1. Insert a Record

**POST**

`/api/dataset/{datasetName}/record`

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

`/api/dataset/employees/query`

Returns all records belonging to the specified dataset.

### 3. Group Records

**GET**

`/api/dataset/employees/query?groupBy=department`

Groups records based on the specified JSON field.

### 4. Sort Records

Ascending:

`/api/dataset/employees/query?sortBy=age&order=asc`

Descending:

`/api/dataset/employees/query?sortBy=salary&order=desc`

### Query Rules

- `groupBy` and `sortBy` cannot be used together.
- Sorting supports `asc` and `desc`.
- Without query parameters, all dataset records are returned.
- Numeric, string, and boolean values are supported for sorting.
- Missing and null values are placed last during sorting.
- Dataset names must start with a letter and contain only letters, numbers, hyphens, or underscores.

## Root Endpoint

**GET /**

Returns application information and its current running status.

Example response:

```json
{
  "application": "JSON Dataset API",
  "status": "Running",
  "message": "API for storing, grouping and sorting JSON records.",
  "documentation": "/swagger-ui/index.html"
}
```

## Swagger Documentation

Interactive API documentation is available at:

https://json-dataset-api-somw.onrender.com/swagger-ui/index.html

Swagger allows users to explore endpoints, provide request parameters, execute API requests, and inspect responses.

## Local Setup

### Prerequisites

- Java 17
- Maven
- PostgreSQL

### 1. Clone the Repository

```bash
git clone https://github.com/Sanskar3008/json-dataset-api.git
```

Navigate to the Spring Boot application directory:

```bash
cd json-dataset-api/json-dataset-api
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE json_dataset_db;
```

Create a local configuration file:

`src/main/resources/application-local.properties`

Add your local database configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/json_dataset_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

Replace `YOUR_PASSWORD` with your local PostgreSQL password.

**Note:** Keep your local credentials private. Do not commit `application-local.properties` to GitHub.

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Activate the `local` Spring profile in your IDE using:

```text
--spring.profiles.active=local
```

The application runs at:

http://localhost:8080

Swagger documentation:

http://localhost:8080/swagger-ui/index.html

## Running Tests

Execute the automated tests using:

```bash
mvn clean test
```

The project includes unit and controller tests using JUnit 5, Mockito, and MockMvc.

## Deployment

The application is deployed using Docker on Render.

- Java 17 Docker build and runtime images.
- PostgreSQL hosted database.
- Environment-based database configuration.
- Automatic deployment from the GitHub main branch.

The following environment variables are configured in Render:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Database credentials are managed through Render environment variables and are not stored in the source code.

## Error Handling

The application provides centralized error handling for:

- Invalid request parameters — `400 Bad Request`
- Invalid JSON request bodies — `400 Bad Request`
- Unknown endpoints — `404 Not Found`
- Unexpected server errors — `500 Internal Server Error`

## Repository

GitHub: https://github.com/Sanskar3008/json-dataset-api
