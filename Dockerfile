FROM maven:3.8.6-openjdk-18 AS build
RUN curl -fsSL -o /opt/dd-java-agent.jar https://github.com/DataDog/dd-trace-java/releases/latest/download/dd-java-agent.jar
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar
COPY --from=build /opt/dd-java-agent.jar /dd/dd-java-agent.jar
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/dd/dd-java-agent.jar","-Ddd.logs.injection=true","-Ddd.trace.sample.rate=1","-Ddd.service=dds-fuentes","-Ddd.env=prod","-jar","app.jar"]