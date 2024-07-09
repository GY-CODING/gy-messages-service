# Use a base image with Java and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline -B

# Copy the source code to the working directory
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lightweight base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the current directory
COPY --from=build /app/target/*.jar ./gy-accounts.jar

# Expose the port on which the application will run
EXPOSE 8002

# Set the command to run the application
CMD ["java", "-jar", "gy-accounts.jar"]