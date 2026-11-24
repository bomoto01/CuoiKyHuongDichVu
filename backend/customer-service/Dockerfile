FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/customer-service/target/customer-service-1.0.0.jar app.jar
ENV PORT=8083
ENV SPRING_PROFILES_ACTIVE=supabase
EXPOSE 8083
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
