# Build stage
FROM openjdk:23-jdk AS builder
WORKDIR /app

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y xargs

COPY . .

RUN ./gradlew clean build -x test

# Run stage
FROM openjdk:23-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
