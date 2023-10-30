FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -Pproduction

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/autokoolweb-1.0-SNAPSHOT.jar autokoolweb.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","autokoolweb.jar"]
