# 1. Build Stage (Maven)
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Avoid Maven using huge default ~/.m2
ENV MAVEN_OPTS="-Dmaven.repo.local=/app/.m2"

WORKDIR /app

# Copy pom.xml first (cache dependencies)
COPY pom.xml .
# Copy application source
COPY src ./src

# Build JAR (downloads dependencies automatically)
RUN mvn -q -B -e clean package -DskipTests

# 2. Runtime Stage (Distroless-like)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy JAR from builder stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8082

# Allow profile override (instance, instance1, instance2,...)
ENV SPRING_PROFILES_ACTIVE=default

# Run the app safely
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
