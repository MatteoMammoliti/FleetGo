FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# COPY Maven wrapper files FIRST
COPY mvnw .
COPY .mvn .mvn

# Copy project files
COPY pom.xml .
COPY src ./src

# Ensure mvnw is executable
RUN chmod +x mvnw

# Build
RUN ./mvnw -q -e -DskipTests package

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]