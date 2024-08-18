FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
COPY ./build/libs/SlimDealz-0.0.1-SNAPSHOT /app/app.jar

ENV TZ=Asia/Seoul

CMD ["java", "-jar", "app.jar"]











