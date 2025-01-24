FROM openjdk:17-jdk

COPY target/wisemoney.jar .

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "wisemoney.jar"]