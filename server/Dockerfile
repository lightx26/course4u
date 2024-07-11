FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/server-0.0.1-SNAPSHOT.jar ./

# Create uploads directory
RUN mkdir -p ./uploads

# Ensure permissions
RUN chmod -R 777 ./uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
