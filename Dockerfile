## 빌드 단계
#FROM ubuntu:20.04 AS builder
#
## 필수 패키지 설치 및 OpenJDK 23 설치
#RUN apt-get update && \
#    apt-get install -y wget gnupg lsb-release && \
#    wget https://download.oracle.com/java/24/latest/jdk-24_linux-x64_bin.deb && \
#    dpkg -i jdk-23_linux-x64_bin.deb && \
#    apt-get install -f && \
#    apt-get clean
#
## Gradle 설치
#RUN apt-get update && \
#    apt-get install -y gradle && \
#    apt-get clean
#
#WORKDIR /app
#
## 소스 코드 복사
#COPY . .
#
## Gradle 빌드
#RUN ./gradlew clean build -x test
#
## 실행 단계
#FROM openjdk:23-jdk
#
#ENV TZ=Asia/Seoul
#
#WORKDIR /app
#
## 빌드한 JAR 파일 복사
#COPY --from=builder /app/build/libs/*.jar app.jar
#
#EXPOSE 28467
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Builder - 애플리케이션 빌드 환경 (Temurin JDK 23 사용)
FROM eclipse-temurin:23-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 전체 파일 복사 (gradlew, 소스코드 등)
# .dockerignore 파일을 사용하여 불필요한 파일(예: .git, build) 복사 방지 권장
COPY . .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행 (JDK 23 환경에서 실행)
# build.gradle에 Java 23 Toolchain 설정 권장
# java {
#     toolchain {
#         languageVersion = JavaLanguageVersion.of(23)
#     }
# }
RUN ./gradlew clean build -x test

# Stage 2: Runtime - 애플리케이션 실행 환경 (Temurin JRE 23 사용)
# JRE 사용으로 이미지 크기 최적화
FROM eclipse-temurin:23-jre

# 타임존 설정
ENV TZ=Asia/Seoul

# 작업 디렉토리 설정
WORKDIR /app

# 빌더 스테이지에서 빌드된 JAR 파일 복사
# build/libs/*.jar 패턴이 정확한지 확인 (보통 Spring Boot는 'app.jar' 하나만 생성)
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 포트 노출 (실제 사용하는 포트로 변경)
EXPOSE 28467

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]