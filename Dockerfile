FROM openjdk:21

RUN apt-get update && apt-get install -y \
    openjfx \

WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]