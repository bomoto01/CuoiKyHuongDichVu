FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/borrow-service/target/borrow-service-1.0.0.jar app.jar
ENV PORT=8084
ENV SPRING_PROFILES_ACTIVE=supabase
EXPOSE 8084
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
