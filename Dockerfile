# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk
ARG PROFILE=dev

WORKDIR /app

# build stage'dan birinchi .jar faylni app.jar nomi bilan nusxalash
COPY --from=build /build/target/*.jar app.jar

# port ochish
EXPOSE 8088

# Atrof-muhit o‘zgaruvchilari
ENV DB_URL=jdbc:postgresql://postgres:5432/loan_contracts
ENV MAILDEV_URL=localhost
ENV ACTIVE_PROFILE=${PROFILE}

# Spring Boot ilovasini ishga tushirish
CMD ["java", "-jar", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-Dspring.datasource.url=${DB_URL}", "app.jar"]
