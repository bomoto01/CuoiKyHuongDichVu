# ==========================================
# 1. BUILD STAGE (Maven + JDK 21)
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy backend pom and source code from repository root
COPY backend/pom.xml ./pom.xml
RUN mvn dependency:go-offline -B || true

COPY backend/src ./src
RUN mvn clean package -DskipTests

# ==========================================
# 2. RUNTIME STAGE (Eclipse Temurin JRE 21)
# ==========================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy executable jar from builder stage
COPY --from=builder /workspace/target/library-management-1.0.0.jar app.jar

# Configuration for Render / Cloud environments
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=supabase

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]

