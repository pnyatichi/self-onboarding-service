# Start with a base image containing Java runtime
FROM openjdk:11
# Add Maintainer Info
LABEL maintainer="Philip Nyatichi <pnyatichi@co-opbank.co.ke>"

#Switch to /app directory
WORKDIR /app
# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/self-onboarding.jar

# Add the application's jar to the container
COPY ./target/self-onboarding.jar app.jar

#Run jar file
ENTRYPOINT ["java","-jar","/app.jar"]
