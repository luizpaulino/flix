FROM amazoncorretto:17-alpine-jdk
LABEL authors="Luiz"

COPY target/flix-0.0.1-SNAPSHOT.jar flix.jar
COPY application.yml application.yml
ENTRYPOINT ["java","-jar","/flix.jar"]