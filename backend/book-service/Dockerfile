FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/book-service/target/book-service-1.0.0.jar app.jar
ENV PORT=8082
ENV SPRING_PROFILES_ACTIVE=supabase
EXPOSE 8082
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
