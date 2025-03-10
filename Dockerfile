FROM openjdk:21

RUN apt-get update && apt-get install -y \
    openjfx \

CMD /usr/bin/java -jar /app/app.jar
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]