FROM openjdk:11-jdk

EXPOSE 8080

COPY ./build/libs/Cinema-0.0.1-SNAPSHOT-plain.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "Cinema-0.0.1-SNAPSHOT-plain.jar"]
