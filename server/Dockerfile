# Stage 1: Build
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/server-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
