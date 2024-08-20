# Use a base image with Java and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

ARG SPRING_PROFILE
ARG SPRING_PORT
ARG CHAT_MONGO_URI
ARG GY_ACCOUNTS_URL
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN
ARG AUTH0_MAIN_DOMAIN
ARG AUTH0_MAIN_CLIENTID
ARG AUTH0_MAIN_CLIENTSECRET
ARG AUTH0_MANAGEMENT_URL
ARG AUTH0_MANAGEMENT_CLIENTID
ARG AUTH0_MANAGEMENT_CLIENTSECRET
ARG AUTH0_MANAGEMENT_TOKEN_URL
ARG AUTH0_MANAGEMENT_REVOKE_URL
ARG AUTH0_GOOGLE_CALLBACK
ARG AUTH0_USERINFO_URL

ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SPRING_PORT=${SPRING_PORT}
ENV CHAT_MONGO_URI=${CHAT_MONGO_URI}
ENV GY_ACCOUNTS_URL=${GY_ACCOUNTS_URL}
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}
ENV AUTH0_MAIN_DOMAIN=${AUTH0_MAIN_DOMAIN}
ENV AUTH0_MAIN_CLIENTID=${AUTH0_MAIN_CLIENTID}
ENV AUTH0_MAIN_CLIENTSECRET=${AUTH0_MAIN_CLIENTSECRET}
ENV AUTH0_MANAGEMENT_URL=${AUTH0_MANAGEMENT_URL}
ENV AUTH0_MANAGEMENT_CLIENTID=${AUTH0_MANAGEMENT_CLIENTID}
ENV AUTH0_MANAGEMENT_CLIENTSECRET=${AUTH0_MANAGEMENT_CLIENTSECRET}
ENV AUTH0_MANAGEMENT_TOKEN_URL=${AUTH0_MANAGEMENT_TOKEN_URL}
ENV AUTH0_MANAGEMENT_REVOKE_URL=${AUTH0_MANAGEMENT_REVOKE_URL}
ENV AUTH0_GOOGLE_CALLBACK=${AUTH0_GOOGLE_CALLBACK}
ENV AUTH0_USERINFO_URL=${AUTH0_USERINFO_URL}


# Copy the settings.xml file to the Maven configuration directory
COPY src/main/resources/settings.xml /root/.m2/settings.xml

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml

# Copy the source code to the working directory
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lightweight base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

ARG SPRING_PROFILE
ARG SPRING_PORT
ARG FOTG_MONGO_URI
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN
ARG AUTH0_MAIN_DOMAIN
ARG AUTH0_MAIN_CLIENTID
ARG AUTH0_MAIN_CLIENTSECRET
ARG AUTH0_MANAGEMENT_URL
ARG AUTH0_MANAGEMENT_CLIENTID
ARG AUTH0_MANAGEMENT_CLIENTSECRET
ARG AUTH0_MANAGEMENT_TOKEN_URL
ARG AUTH0_MANAGEMENT_REVOKE_URL
ARG AUTH0_GOOGLE_CALLBACK

ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SPRING_PORT=${SPRING_PORT}
ENV FOTG_MONGO_URI=${FOTG_MONGO_URI}
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}
ENV AUTH0_MAIN_DOMAIN=${AUTH0_MAIN_DOMAIN}
ENV AUTH0_MAIN_CLIENTID=${AUTH0_MAIN_CLIENTID}
ENV AUTH0_MAIN_CLIENTSECRET=${AUTH0_MAIN_CLIENTSECRET}
ENV AUTH0_MANAGEMENT_URL=${AUTH0_MANAGEMENT_URL}
ENV AUTH0_MANAGEMENT_CLIENTID=${AUTH0_MANAGEMENT_CLIENTID}
ENV AUTH0_MANAGEMENT_CLIENTSECRET=${AUTH0_MANAGEMENT_CLIENTSECRET}
ENV AUTH0_MANAGEMENT_TOKEN_URL=${AUTH0_MANAGEMENT_TOKEN_URL}
ENV AUTH0_MANAGEMENT_REVOKE_URL=${AUTH0_MANAGEMENT_REVOKE_URL}
ENV AUTH0_GOOGLE_CALLBACK=${AUTH0_GOOGLE_CALLBACK}


# Copy the settings.xml file to the Maven configuration directory
COPY src/main/resources/settings.xml /root/.m2/settings.xml

# Copy the JAR file from the build stage to the current directory
COPY --from=build /app/target/*.jar ./gy-messages.jar

# Ensure the JAR file has the correct permissions
RUN chmod 777 /app/gy-messages.jar

# Expose the port on which the application will run
EXPOSE 8002

# Set the command to run the application
CMD ["java", "-jar", "gy-messages.jar"]
