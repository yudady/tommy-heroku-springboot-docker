FROM amazoncorretto:17

ARG port
USER root

ENV PORT=$port
COPY ./target/*.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-Dserver.port=$port","-jar","app.jar"]

EXPOSE $PORT

