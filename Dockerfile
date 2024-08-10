FROM openjdk:17

WORKDIR /app

COPY target/my-app.jar /app/app.jar

CMD ["java", "-jar","app.jar"]