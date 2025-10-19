FROM eclipse-temurin:21-alpine
COPY build/libs/*.jar app.jar

ENV TZ Asia/Seoul
ARG ENV

ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "-Dserver.env=${ENV}", "app.jar"]