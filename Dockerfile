FROM gradle:8.1.1-jdk17 AS build

WORKDIR /home/gradle/project

COPY . .

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY .env /app/.env

COPY --from=build /home/gradle/project/build/libs/SlimDealz-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 8080














