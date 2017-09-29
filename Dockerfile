FROM java:8

MAINTAINER Dmitry Yakubovsky <icubedm@gmail.com>

ENV SPRING_PROFILES_ACTIVE=production \
    TIME_RANGE_SECONDS=60 \

COPY app/target/app*.jar app/app.jar

WORKDIR /app

VOLUME /app/logs

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
