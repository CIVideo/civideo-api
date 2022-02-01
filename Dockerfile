FROM openjdk:11.0.12-jre

EXPOSE 80

VOLUME /tmp/log/civideo

COPY build/libs/*.jar /home/civideo.jar

ENTRYPOINT java -jar /home/civideo.jar