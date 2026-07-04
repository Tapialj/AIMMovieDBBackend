# Use Official Maven image of jdk 17
FROM maven:3.9.16-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom, update version, and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build app
COPY src ./src
RUN mvn clean package -DskipTests

# Use official OpenJDK image to run app
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy built jar file from build stage
COPY --from=build /app/target/capstone.jar .

# Expose 8080
EXPOSE 8080

# Specify command to run app
ENTRYPOINT ["java", "-jar", "/app/capstone.jar"]