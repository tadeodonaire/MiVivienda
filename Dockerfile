# Build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# (Opcional) tunear memoria de la JVM desde env:
ENV JAVA_OPTS=""

# Expón el puerto típico
EXPOSE 8080

# IMPORTANTE: respeta el puerto que Azure pone en $PORT
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
