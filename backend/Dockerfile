# ==========================================
# 1. BUILD STAGE (Maven + JDK 21)
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Configure Maven settings to prevent 429 Too Many Requests
COPY backend/.mvn/settings.xml /root/.m2/settings.xml

# Copy parent POM and all microservices modules
COPY backend/pom.xml ./pom.xml
COPY backend/common-library ./common-library
COPY backend/account-service ./account-service
COPY backend/book-service ./book-service
COPY backend/customer-service ./customer-service
COPY backend/borrow-service ./borrow-service
COPY backend/gateway-service ./gateway-service

# Build all microservices with custom mirror
RUN mvn clean package -DskipTests -s /root/.m2/settings.xml

# ==========================================
# 2. RUNTIME STAGE (Eclipse Temurin JRE 21)
# ==========================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy all 5 service JARs from builder
COPY --from=builder /workspace/account-service/target/account-service-1.0.0.jar account.jar
COPY --from=builder /workspace/book-service/target/book-service-1.0.0.jar book.jar
COPY --from=builder /workspace/customer-service/target/customer-service-1.0.0.jar customer.jar
COPY --from=builder /workspace/borrow-service/target/borrow-service-1.0.0.jar borrow.jar
COPY --from=builder /workspace/gateway-service/target/gateway-service-1.0.0.jar gateway.jar

# Entrypoint script
COPY backend/entrypoint.sh ./entrypoint.sh
RUN sed -i 's/\r$//' ./entrypoint.sh && chmod +x ./entrypoint.sh

# Configuration for Render / Cloud environments
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=supabase

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
