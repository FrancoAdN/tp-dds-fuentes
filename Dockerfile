FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar
# Fetch Datadog Java agent directly (avoids build context issues)
ADD https://dtdg.co/latest-java-tracer dd-java-agent.jar
EXPOSE 8080
ENV DD_SERVICE=my-app-name \
    DD_ENV=dev \
    DD_VERSION=1.0.0 \
    DATADOG_API_KEY=2a2041e18c844801dbaa09f884aa26d2 \
    DD_SITE=datadoghq.com
ENTRYPOINT ["java", "-jar", "app.jar"]