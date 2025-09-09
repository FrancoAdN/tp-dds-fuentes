FROM maven:3.8.6-openjdk-18 AS build

RUN curl -Lo dd-java-agent.jar https://dtdg.co/latest-java-tracer 

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar


ENV DD_SITE=datadoghq.com \
    DD_API_KEY=2a2041e18c844801dbaa09f884aa26d2 \
    DD_AGENT_HOST=datadog-agent \
    DD_TRACE_AGENT_PORT=8126
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:../dd-java-agent.jar","-Ddd.trace.sample.rate=1","-Ddd.service=dds-fuentes","-Ddd.env=prod","-Ddd.site=datadoghq.com","-jar","app.jar"]