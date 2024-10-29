# Build the JAR file
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Run the application
FROM maven:3.8.8-eclipse-temurin-21
WORKDIR /app
COPY --from=build /app/target/lab3_3-0.0.1-SNAPSHOT.jar  app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]