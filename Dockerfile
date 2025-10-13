# 1. Build Stage: Amazon Corretto JDK로 프로젝트를 빌드합니다.
FROM amazoncorretto:17-al2-jdk as builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# 2. Run Stage: 더 가벼운 Amazon Corretto JRE 이미지를 사용합니다.
FROM amazoncorretto:17-al2-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 실행 시 Spring Boot의 'prod' 프로필을 활성화합니다.
# 이 부분이 application-prod.yml 설정을 읽도록 만드는 핵심입니다!
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]