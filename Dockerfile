# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.14-amazoncorretto-25-debian AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
# Sửa dòng 11 thành:
RUN mvn package -Dmaven.test.skip=true -Dspotless.check.skip=true

#Stage 2: create image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:25-alpine

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]