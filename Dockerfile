FROM openjdk:17-jdk-alpine

RUN mkdir /app
WORKDIR /app

COPY target/*.jar /app/app.jar
COPY src/main/resources/templates /app/classes/templates
COPY src/main/resources/static /app/classes/static

CMD ["java", "-jar", "/app/app.jar"]
