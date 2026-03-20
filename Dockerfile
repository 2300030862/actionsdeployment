# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 as builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/employees/stats/total || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]

