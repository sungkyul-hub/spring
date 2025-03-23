# Build stage

FROM openjdk:23-jdk AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test


# Run stage

FROM openjdk:23-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]