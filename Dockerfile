FROM gradle:6.8-jdk15 AS build
COPY --chown=gradle:gradle . gradle/src
WORKDIR gradle/src
RUN gradle build --no-daemon 

FROM openjdk:15-alpine
COPY build/libs/*.jar app.jar
EXPOSE server.port
ENTRYPOINT ["java", "-jar", "app.jar",  "--spring.config.location=file:${configDirectory}/application.properties"]
