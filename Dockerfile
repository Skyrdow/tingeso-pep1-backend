FROM openjdk:17
ARG JAR_FILE=target/autofix-backend.jar
COPY ${JAR_FILE} autofix-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/autofix-backend.jar"]