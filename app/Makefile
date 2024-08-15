.DEFAULT_GOAL := build-run

backend:
	./gradlew bootRun --args='--spring.profiles.active=development'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=production'

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

update-deps:
	./gradlew useLatestVersions

build-run: build run

.PHONY: build