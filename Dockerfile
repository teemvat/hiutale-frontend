FROM ubuntu:20.04

# Install OpenJDK 11, OpenJFX, and X11 apps
RUN apt-get update && apt-get install -y openjdk-21-jdk openjfx x11-apps

WORKDIR /app
COPY target/frontend-1.0-SNAPSHOT.jar /app/frontend.jar

ENV DISPLAY=host.docker.internal:0.0

ENTRYPOINT ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/app/frontend.jar"]
