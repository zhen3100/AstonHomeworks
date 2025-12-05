# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Кэширование зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Stage 2: Extract layers
FROM builder as extractor
WORKDIR /app
RUN java -Djarmode=layertools -jar target/*.jar extract

# Stage 3: Runtime
FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="Your Name <your.email@example.com>"

WORKDIR /app

# Копируем слои JAR
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Запускаем приложение
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]