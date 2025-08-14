FROM gradle:7.6-jdk17-alpine AS builder

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle clean build -x test

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/task-management.jar app.jar
COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
