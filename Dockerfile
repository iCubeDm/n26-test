FROM java:8

MAINTAINER Dmitry Yakubovsky <icubedm@gmail.com>

COPY app/target/app*.jar app/app.jar

WORKDIR /app

VOLUME /app/logs

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
