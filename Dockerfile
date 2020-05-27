FROM openjdk:8-jdk-alpine
RUN addgroup -S webadmin && adduser -S webadmin -G webadmin
USER webadmin:webadmin
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 18080
ENTRYPOINT ["java","-jar","/app.jar"]