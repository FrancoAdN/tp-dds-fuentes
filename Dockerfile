FROM maven:3.8.6-openjdk-18 AS build
RUN curl -sSL -o /opt/opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.5.0/opentelemetry-javaagent.jar
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar
COPY --from=build /opt/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/otel/opentelemetry-javaagent.jar","-Dotel.traces.exporter=otlp","-Dotel.metrics.exporter=none","-Dotel.exporter.otlp.protocol=http/protobuf","-Dotel.exporter.otlp.endpoint=https://otlp.us5.datadoghq.com","-Dotel.exporter.otlp.headers=DD-API-KEY=c30a102904646d515381812c83f5beb1","-Dotel.service.name=fuentes","-Dotel.resource.attributes=deployment.environment=render","-jar","app.jar"]