#define base docker image
FROM openjdk:20

LABEL maintainder="hoanglong"

ADD target/capstone-0.0.1-SNAPSHOT.jar capstone.jar
ENTRYPOINT ["java", "-jar", "capstone.jar"]
