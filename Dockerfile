# Build stage
FROM ubuntu:20.04 AS builder

# Java 설치
RUN apt-get update && \
    apt-get install -y openjdk-23-jdk gradle xargs && \
    apt-get clean

WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드
RUN ./gradlew clean build -x test

# Run stage
FROM openjdk:23-jdk

WORKDIR /app

# 빌드한 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
