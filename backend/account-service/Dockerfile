FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/account-service/target/account-service-1.0.0.jar app.jar
ENV PORT=8081
ENV SPRING_PROFILES_ACTIVE=supabase
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
