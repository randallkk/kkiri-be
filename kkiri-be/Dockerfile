FROM gradle:jdk11 as builder

ENV APP_HOME=/apps

WORKDIR $APP_HOME

COPY build.gradle settings.gradle $APP_HOME/
COPY src $APP_HOME/src

ENV TZ=Asia/Seoul
RUN gradle clean build -x test

FROM openjdk:11-jdk

ENV APP_HOME=/apps

WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/kkiri-0.0.1-SNAPSHOT.jar app.jar

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]