FROM openjdk:17
ARG JAR_FILE=target/demo-1.jar
COPY ${JAR_FILE} demo-1.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "/demo-1.jar"]