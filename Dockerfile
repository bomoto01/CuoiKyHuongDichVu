# ==========================================
# 1. BUILD STAGE (Maven + JDK 21)
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Configure Maven settings to prevent 429 Too Many Requests
COPY backend/.mvn/settings.xml /root/.m2/settings.xml

# Copy Render single-JVM POM and source code (fits 512MB Free Tier RAM)
COPY backend/pom-render.xml ./pom.xml
COPY backend/src ./src

# Build single executable JAR
RUN mvn clean package -DskipTests -s /root/.m2/settings.xml

# ==========================================
# 2. RUNTIME STAGE (Eclipse Temurin JRE 21)
# ==========================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/target/library-management-1.0.0.jar app.jar

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=supabase

EXPOSE 8080

# Tune JVM memory strictly within Render 512MB Free Tier limit
ENTRYPOINT ["sh", "-c", "java -Xms64m -Xmx320m -XX:+UseSerialGC -Dserver.port=${PORT} -Dfile.encoding=UTF-8 -jar app.jar"]
