FROM openjdk:11

EXPOSE 8080

COPY build/libs/app.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]