FROM eclipse-temurin:21-alpine as builder

WORKDIR /home

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /home/build/libs/weather-api-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]