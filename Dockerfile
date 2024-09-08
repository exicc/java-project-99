FROM node:20.6.1 AS frontend

WORKDIR /frontend

RUN npm i @hexlet/java-task-manager-frontend

RUN npx build-frontend

FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -yq make unzip && rm -rf /var/lib/apt/lists/*

WORKDIR /backend

COPY config/checkstyle/checkstyle.xml config/checkstyle/
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src/main/resources/certs/private.pem certs/
COPY src/main/resources/certs/public.pem certs/

RUN ./gradlew --no-daemon dependencies

COPY lombok.config .
COPY src/ src/

COPY --from=frontend /frontend/src/main/resources/static /backend/src/main/resources/static

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS="-Xmx512M -Xms512M"

EXPOSE 8080

CMD java $JAVA_OPTS -jar build/libs/*-SNAPSHOT.jar