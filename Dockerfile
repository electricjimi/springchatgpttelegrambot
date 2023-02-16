FROM amazoncorretto:11

ARG JAR_FILE=target/spring2bot-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]