FROM eclipse-temurin:17 AS build

RUN apt-get update && apt-get install -y --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG="/root/.m2"

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw .

COPY pom.xml /app/pom.xml
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    ./mvnw dependency:go-offline

COPY . /app
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre-alpine

RUN apk add --no-cache python3 py3-pip supervisor

COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

COPY config/supervisord.conf /etc/supervisord.conf

EXPOSE 8080

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]