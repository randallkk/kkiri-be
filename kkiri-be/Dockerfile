FROM gradle:jdk11 as builder

ENV APP_HOME=/apps

WORKDIR $APP_HOME

COPY build.gradle settings.gradle $APP_HOME/

COPY src $APP_HOME/src

RUN gradle clean build -x test

FROM openjdk:11-jdk

ENV APP_HOME=/apps
ARG JAR_FILE_PATH=build/libs/kkiri-0.0.1-SNAPSHOT.jar

WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/$JAR_FILE_PATH app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]