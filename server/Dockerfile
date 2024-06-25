# Stage 1: Build
# Stage 2: Run
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/server-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
