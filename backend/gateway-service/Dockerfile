FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/gateway-service/target/gateway-service-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
