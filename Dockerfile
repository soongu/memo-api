# 1. 단일 스테이지 빌드: Amazon Corretto JDK 이미지 하나로 빌드와 실행을 모두 처리합니다.
FROM amazoncorretto:17-al2-jdk

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 현재 디렉토리의 모든 파일을 컨테이너의 /app 디렉토리로 복사합니다.
COPY . .

# Gradle을 사용하여 프로젝트를 빌드합니다.
RUN ./gradlew clean build

# 빌드된 jar 파일의 이름을 app.jar로 변경하고 /app 디렉토리로 이동시킵니다.
RUN mv /app/build/libs/*.jar /app/app.jar

# 컨테이너 실행 시 Spring Boot의 'prod' 프로필을 활성화하고,
# 빌드된 JAR 파일을 직접 실행합니다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

