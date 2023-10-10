FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8000

COPY --from=build /build/libs/erp-0.0.1-SNAPSHOT.jar erp.jar

ENTRYPOINT ["java", "-jar", "erp.jar"]