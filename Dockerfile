# Build stage
FROM openjdk:23-jdk AS builder
RUN apt-get update && apt-get install -y --no-install-recommends findutils xargs build-essential && rm -rf /var/lib/apt/lists/*
WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# Run stage
FROM openjdk:23-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
