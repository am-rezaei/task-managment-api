# Task Management API

A simple Spring Boot REST API for managing tasks, built with Spring Boot 3 for creating APIs and PostgreSQL as the database.

The project is split into several packages so everything has its own place, which makes the code easier to maintain and develop.

- **/controller**: Handles incoming HTTP requests

- **/service**: Contains the business logic of the application

- **/repository**: Handles persisting and fetching data from the database

- **/model**: Defines the database entity models and the DTO objects used to communicate with the API

- **/exception**: Contains custom exceptions for the application and a global handler defining how to manage each type of exception

DTO Pattern is used in this project so APIs and database aren’t tied together tightly. With this approach, if the database schema changes, it won't break the API. At the same time, we can generate the output which is more suitable for client application.

For transforming data between entities and DTOs, I used MapStruct. It performs all the mappings automatically, making the code cleaner and less error-prone.

To handle exceptions in the application, a Global Exception Handler is defined using @RestControllerAdvice annotation. This means we have one place to catch and format all the errors the application might throw, so the API will always provide a consistent response when something goes wrong.


## Running with Docker
To run this project locally, you need to have Docker installed in your system. Then clone the repository and navigate to project folder in your terminal and then run the following commands:

### 1. Build the Docker images
```
docker-compose build
```

### 2. Start the containers
```
docker-compose up
```

## API Endpoints

### Create Task
```
curl -X POST http://localhost:8080/api/tasks   -H "Content-Type: application/json"   -d '{
    "title": "Test Task 1",
    "description": "Description for task 1",
    "status": "PENDING"
  }'
```



### Get All Tasks
```
curl http://localhost:8080/api/tasks
```



### Get Task by ID
```
curl http://localhost:8080/api/tasks/1
```



### Update Task

**Note the provided ID in the URL**
```
curl -X PUT http://localhost:8080/api/tasks/1   -H "Content-Type: application/json"   -d '{
    "title": "Updated Test Task 1",
    "description": "Updated description",
    "status": "COMPLETED"
  }'
```



### Delete Task
```
curl -X DELETE http://localhost:8080/api/tasks/1
```



## Swagger API Documentation
After running the app, Open these urls:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`



##  Running Tests

**Before running tests, make sure the docker image of PostgreSQL is running!**
```
./gradlew test
```

