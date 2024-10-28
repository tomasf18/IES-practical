# Build the JAR file
FROM openjdk:21 AS build
# Create a directory for the application inside the docker container
WORKDIR /app
# Copy the pom.xml file to the container
COPY pom.xml ./app
# Copy the source code to the container
COPY src ./app/src
# Build the JAR file inside the container
RUN mvn clean package -DskipTests

# Run the application
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/lab3_3-0.0.1-SNAPSHOT.jar /app/lab3_3.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
