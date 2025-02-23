FROM maven:3.9.0-eclipse-temurin-19 AS build
WORKDIR /app
COPY challange/pom.xml .
RUN mvn dependency:go-offline
COPY challange/src ./src
RUN mvn package -DskipTests

FROM openjdk:19-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker build -t challenge .
# docker run -p 8080:8080 challenge
