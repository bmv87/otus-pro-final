FROM openjdk:21-jdk
EXPOSE 8585

ADD ./build/libs/*.jar otus-pro-final.jar
ENTRYPOINT ["java", "-jar", "/otus-pro-final.jar"]