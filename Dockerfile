FROM gradle:8.1.1-jdk17 as build

COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts /home/gradle/project/

WORKDIR /home/gradle/project
RUN gradle build -x test --no-daemon || return 0

COPY --chown=gradle:gradle . /home/gradle/project/
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 8080
