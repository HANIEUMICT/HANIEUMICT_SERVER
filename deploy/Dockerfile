FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /app/build/libs/*.jar app.jar

ENV TZ Asia/Seoul
ARG ENV

ENTRYPOINT java -jar -Dserver.env=${ENV} app.jar