FROM gradle:6.8-jdk15 AS clean
FROM gradle:6.8-jdk15 AS build
COPY --chown=gradle:gradle . gradle/src
WORKDIR gradle/src
RUN gradle build --no-daemon 

FROM openjdk:15-alpine
COPY build/libs/*.jar app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "app.jar"]
