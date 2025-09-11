FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar
# Fetch Datadog Java agent directly (avoids build context issues)
ADD https://dtdg.co/latest-java-tracer dd-java-agent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]