# Use an official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from your Maven project into the container
COPY target/any-city-forecast-1.0-SNAPSHOT.jar /app/any-city-forecast.jar

# Define the command to run the JAR file
CMD ["java", "-jar", "any-city-forecast.jar"]

