FROM amazoncorretto:17-alpine3.17

ARG MAVEN_VERSION=3.9.9
RUN apk add --no-cache curl tar bash \
    && mkdir -p /usr/share/maven \
    && curl -fsSL https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG="/root/.m2"
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

WORKDIR /app

RUN mkdir -p /root/.m2

COPY pom.xml /app/pom.xml
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn dependency:resolve dependency:resolve-plugins -B

COPY . /app

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn clean install -Dmaven.test.skip=true

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run"]