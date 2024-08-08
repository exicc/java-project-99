# Этап сборки frontend
FROM node:20.6.1 AS frontend

WORKDIR /frontend

COPY frontend/package*.json ./

RUN npm ci

COPY frontend/ .

RUN npm run build

FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -yq make unzip && rm -rf /var/lib/apt/lists/*

WORKDIR /backend

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN ./gradlew --no-daemon dependencies

COPY lombok.config .
COPY src/ src/

COPY --from=frontend /frontend/dist /backend/src/main/resources/static

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS="-Xmx512M -Xms512M"

EXPOSE 8080

CMD java $JAVA_OPTS -jar build/libs/*-SNAPSHOT.jar
